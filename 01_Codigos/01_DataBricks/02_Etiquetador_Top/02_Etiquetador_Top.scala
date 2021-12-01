// Databricks notebook source
// DBTITLE 1,Etiquetador (Top)
"""
El Etiquetador (Top) asignará una etiqueta a cada mensaje de la base que reciba como insumo, dicha etiqueta corresponde a un tópico o tema del diccionario que reciba como insumo, esto por medio de la intersección de bolsas de N-gramas, tomando el mejor nivel de afinidad, la cual se puede usar como referencia para elegir las mejores afinidades, definiendo un top de mejores etiquetas según convenga.
 
Este script está escrito en Pyspark
"""

// COMMAND ----------

// MAGIC %python
// MAGIC pip install openpyxl

// COMMAND ----------

// MAGIC %python
// MAGIC import numpy as np
// MAGIC import pandas as pd
// MAGIC import openpyxl

// COMMAND ----------

// MAGIC %python
// MAGIC # Habilitar la configuración Arrow-based spark
// MAGIC spark.conf.set("spark.sql.execution.arrow.enabled", "true")

// COMMAND ----------

// MAGIC %python
// MAGIC dbutils.fs.unmount("/mnt/parce")

// COMMAND ----------

// DBTITLE 1,Conexión a contenedor en DataLake
// MAGIC %python
// MAGIC """
// MAGIC Conexión al contenedor en DataLake.
// MAGIC """
// MAGIC 
// MAGIC # Azure Storage Account Name
// MAGIC storage_account_name = "dlsfnaparce"
// MAGIC 
// MAGIC # Azure Storage Account Key
// MAGIC storage_account_key = "1y5feFvWNy6C4YGZlGlR/9wEwLeG51EDyGqsgkOiOLW2AndQmnj4PE7Ri+TgRJIC0Mghu5Z4KgqwjdQHZEhE0w=="
// MAGIC 
// MAGIC # Azure Storage Account Source Container
// MAGIC container = "bases"
// MAGIC 
// MAGIC # Set the configuration details to read/write
// MAGIC spark.conf.set("fs.azure.account.key.{0}.blob.core.windows.net".format(storage_account_name), storage_account_key)

// COMMAND ----------

// MAGIC %fs ls dbfs:/mnt

// COMMAND ----------

// MAGIC %python
// MAGIC 
// MAGIC dbutils.fs.mount(
// MAGIC    source = "wasbs://{0}@{1}.blob.core.windows.net".format(container, storage_account_name),
// MAGIC    mount_point = "/mnt/parce",
// MAGIC    extra_configs = {"fs.azure.account.key.{0}.blob.core.windows.net".format(storage_account_name): storage_account_key})
// MAGIC #------------------
// MAGIC 
// MAGIC """
// MAGIC Rutas de las bases en el contenedor
// MAGIC """
// MAGIC 
// MAGIC display(dbutils.fs.ls("/mnt/parce"))

// COMMAND ----------

// DBTITLE 1,Importar bases
// MAGIC %python
// MAGIC 
// MAGIC """
// MAGIC 1. Importación de las bases
// MAGIC 2. Convertir a dataframe spark
// MAGIC """
// MAGIC 
// MAGIC #------------------- Registros
// MAGIC Data = pd.read_excel('/dbfs/mnt/parce/03_Base_principal_ngrams_v21092021.xlsx',sheet_name='Sheet1', engine='openpyxl')
// MAGIC #print(Data.head(5))
// MAGIC 
// MAGIC #------------------- Diccionario
// MAGIC Dicci = pd.read_excel('/dbfs/mnt/parce/03_Diccionario.xlsx',sheet_name='Sheet1', engine='openpyxl')
// MAGIC #print(Dicci.head(5))
// MAGIC 
// MAGIC #------------- Convertir a Dataframe Spark
// MAGIC Data = spark.createDataFrame(Data)
// MAGIC Dicci = spark.createDataFrame(Dicci)

// COMMAND ----------

// MAGIC %python
// MAGIC 
// MAGIC """
// MAGIC Convertir a columnas de listas las bolsas de Ngramas de 'Data' y 'Dicci'
// MAGIC """
// MAGIC 
// MAGIC #------------- Split: Bolsa Ngram de Data
// MAGIC 
// MAGIC #--- Arreglar col N_gram_usu
// MAGIC from pyspark.sql.functions import regexp_replace
// MAGIC Data = Data.withColumn('N_gram_usu', regexp_replace('N_gram_usu', "[\\[\\]]", ''))
// MAGIC Data = Data.withColumn('N_gram_usu', regexp_replace('N_gram_usu', "'", ''))
// MAGIC Data = Data.withColumn('N_gram_usu', regexp_replace('N_gram_usu', ", ",','))
// MAGIC 
// MAGIC #--- Splitiar
// MAGIC from pyspark.sql.functions import split
// MAGIC Data = Data.withColumn('N_gram_usu', split(Data['N_gram_usu'], ','))
// MAGIC 
// MAGIC #------------- Split: Bolsa Ngram de Dicci
// MAGIC 
// MAGIC #--- Arreglar col N_gram de Dicci
// MAGIC from pyspark.sql.functions import regexp_replace
// MAGIC Dicci = Dicci.withColumn('N_gram', regexp_replace('N_gram', "[\\[\\]]", ''))
// MAGIC Dicci = Dicci.withColumn('N_gram', regexp_replace('N_gram', "'", ''))
// MAGIC Dicci = Dicci.withColumn('N_gram', regexp_replace('N_gram', ", ",','))
// MAGIC 
// MAGIC #--- Splitiar
// MAGIC from pyspark.sql.functions import split
// MAGIC Dicci = Dicci.withColumn('N_gram', split(Dicci['N_gram'], ','))
// MAGIC 
// MAGIC #----------------------------------------------------------------------------
// MAGIC 
// MAGIC """
// MAGIC Crear columna 'Index' para Dicci dataframe
// MAGIC """
// MAGIC 
// MAGIC #------------- Columna: Id_Etiqueta
// MAGIC from pyspark.sql.functions import monotonically_increasing_id, row_number
// MAGIC from pyspark.sql import Window
// MAGIC 
// MAGIC #convert list to a dataframe
// MAGIC rating = list(range(Dicci.count()))
// MAGIC b = sqlContext.createDataFrame([(l,) for l in rating], ['Eti'])
// MAGIC 
// MAGIC #add 'sequential' index and join both dataframe to get the final result
// MAGIC Dicci = Dicci.withColumn("row_idx", row_number().over(Window.orderBy(monotonically_increasing_id())))
// MAGIC b = b.withColumn("row_idx", row_number().over(Window.orderBy(monotonically_increasing_id())))
// MAGIC Dicci = Dicci.join(b, Dicci.row_idx == b.row_idx).drop("row_idx")
// MAGIC 
// MAGIC Dicci = Dicci[['Eti','N_gram','Etiqueta','Servicio','Respuesta']]
// MAGIC Dicci = Dicci.withColumn('Eti', Dicci['Eti'].cast('int'))
// MAGIC 
// MAGIC #Dicci.show()

// COMMAND ----------

// DBTITLE 1,Muestra
// MAGIC %python
// MAGIC """
// MAGIC Elegir la medida de la muestra
// MAGIC """
// MAGIC n = int(230000)
// MAGIC Muestra = Data.sample(fraction = 1.0).limit(n)

// COMMAND ----------

// MAGIC %python
// MAGIC """
// MAGIC Crear columna 'Index' para Muestra dataframe
// MAGIC """
// MAGIC 
// MAGIC from pyspark.sql.functions import monotonically_increasing_id, row_number
// MAGIC from pyspark.sql import Window
// MAGIC 
// MAGIC #convert list to a dataframe
// MAGIC rating = list(range(Muestra.count()))
// MAGIC b = sqlContext.createDataFrame([(l,) for l in rating], ['Interaccion'])
// MAGIC 
// MAGIC #add 'sequential' index and join both dataframe to get the final result
// MAGIC Muestra = Muestra.withColumn("row_idx", row_number().over(Window.orderBy(monotonically_increasing_id())))
// MAGIC b = b.withColumn("row_idx", row_number().over(Window.orderBy(monotonically_increasing_id())))
// MAGIC Muestra = Muestra.join(b, Muestra.row_idx == b.row_idx).drop("row_idx")
// MAGIC 
// MAGIC Muestra = Muestra[['Interaccion','Intencion','Usuario','N_gram_usu']]
// MAGIC Muestra = Muestra.withColumn('Interaccion', Muestra['Interaccion'].cast('int'))

// COMMAND ----------

// MAGIC %python
// MAGIC """
// MAGIC Crear un dataframe Cruz: Producto cruz de las columnas 'Index' de Muestra y Dicci
// MAGIC """
// MAGIC 
// MAGIC from pyspark.sql.types import *
// MAGIC A = list(range(Muestra.count()))
// MAGIC B = list(range(Dicci.count()))
// MAGIC 
// MAGIC ################################################ Primer loop
// MAGIC n = []
// MAGIC list(map( lambda x : n.extend(map(x, B)) , map(lambda x :lambda y: (x,y) , A ) ))
// MAGIC 
// MAGIC #Cruz = spark.createDataFrame(n , ArrayType(IntegerType()) )
// MAGIC #Cruz.show()
// MAGIC 
// MAGIC data = sc.parallelize(n)
// MAGIC #type(data)
// MAGIC 
// MAGIC # Convert to tuple
// MAGIC data_converted = data.map(lambda x: (x[0], x[1]))
// MAGIC 
// MAGIC # Define schema
// MAGIC schema = StructType([
// MAGIC     StructField('Interaccion', IntegerType(), True),
// MAGIC     StructField('Eti', IntegerType(), True)
// MAGIC ])
// MAGIC 
// MAGIC # Create dataframe
// MAGIC Cruz = sqlContext.createDataFrame(data_converted, schema)
// MAGIC 
// MAGIC #Cruz.show()

// COMMAND ----------

// MAGIC %python
// MAGIC """
// MAGIC Cruz3 es el dataframe equivalente a cruzar el dataframe Muestra y el dataframe Dicci
// MAGIC """
// MAGIC 
// MAGIC Cruz2 = (Cruz.join(Muestra, on = ['Interaccion'], how = 'left') )
// MAGIC Cruz3 = (Cruz2.join(Dicci, on = ['Eti'], how = 'left') )
// MAGIC #Cruz3.show()

// COMMAND ----------

// DBTITLE 1,Intersección de bolsas de N-gramas
// MAGIC %python
// MAGIC """
// MAGIC Uso de las funciones spark: Intersección de las bolsas de Ngramas
// MAGIC """
// MAGIC 
// MAGIC from pyspark.sql import functions as F
// MAGIC 
// MAGIC Cruz3 = Cruz3.withColumn("Inter", F.array_intersect("N_gram_usu","N_gram"))
// MAGIC Cruz3 = Cruz3.withColumn("Lon", F.size("Inter"))
// MAGIC #Cruz3.show()

// COMMAND ----------

// MAGIC %python
// MAGIC """
// MAGIC Descartar las interacciones que no cruzaron sus bolsas de Ngramas con Dicci
// MAGIC """
// MAGIC 
// MAGIC from pyspark.sql.functions import col
// MAGIC 
// MAGIC Cruz4 = Cruz3.where(col('Lon') != 0)
// MAGIC #Cruz4.count()

// COMMAND ----------

// MAGIC %python
// MAGIC """
// MAGIC Selecionar la intersección más alta
// MAGIC """
// MAGIC 
// MAGIC j = {'Lon':'max'}
// MAGIC corte = Cruz4.groupby(['Interaccion']).agg(j)
// MAGIC #corte.show()

// COMMAND ----------

// MAGIC %python
// MAGIC """
// MAGIC Cruz5 es el dataframe final con las interacciones que cruzaron.
// MAGIC """
// MAGIC 
// MAGIC Cruz5 = (Cruz4.join(corte, on = (Cruz4['Interaccion'] == corte['Interaccion'])&(Cruz4['Lon'] == corte['max(Lon)']), how = 'inner') )
// MAGIC Cruz5 = Cruz5[['Intencion','Usuario','Etiqueta','Lon']]
// MAGIC 
// MAGIC Cruz5.count()

// COMMAND ----------

// MAGIC %python
// MAGIC from pyspark.sql.functions import col, row_number
// MAGIC from pyspark.sql.window import Window
// MAGIC 
// MAGIC # Rankear el nivel de interseción de n-grams
// MAGIC Cruz6 = Cruz5.select('Intencion','Usuario','Etiqueta','Lon', F.row_number().over(Window.partitionBy("Etiqueta").orderBy(col("Lon").desc())).alias("rowNum"))
// MAGIC 
// MAGIC # Elegir a los sumo los 150 mejores
// MAGIC Cruz7 = Cruz6.where(col('rowNum') <= 150)
// MAGIC Cruz8 = Cruz7[['Intencion','Usuario','Etiqueta']]
// MAGIC #display(Cruz7)

// COMMAND ----------

// MAGIC %python
// MAGIC dbutils.fs.unmount("/mnt/parce")

// COMMAND ----------

// DBTITLE 1,Conectar a contenedor de resultados
// MAGIC %python
// MAGIC """
// MAGIC Conexión al contenedor (resultados) en DataLake.
// MAGIC """
// MAGIC 
// MAGIC #---------
// MAGIC # Azure Storage Account Name
// MAGIC storage_account_name = "dlsfnaparce"
// MAGIC 
// MAGIC # Azure Storage Account Key
// MAGIC storage_account_key = "1y5feFvWNy6C4YGZlGlR/9wEwLeG51EDyGqsgkOiOLW2AndQmnj4PE7Ri+TgRJIC0Mghu5Z4KgqwjdQHZEhE0w=="
// MAGIC 
// MAGIC # Azure Storage Account Source Container
// MAGIC container = "resultados"

// COMMAND ----------

// MAGIC %fs ls dbfs:/mnt

// COMMAND ----------

// MAGIC %python
// MAGIC   dbutils.fs.mount(
// MAGIC    source = "wasbs://{0}@{1}.blob.core.windows.net".format(container, storage_account_name),
// MAGIC    mount_point = "/mnt/parce",
// MAGIC    extra_configs = {"fs.azure.account.key.{0}.blob.core.windows.net".format(storage_account_name): storage_account_key}
// MAGIC   )

// COMMAND ----------

// DBTITLE 1,Exportar bases
// MAGIC %python
// MAGIC 
// MAGIC """
// MAGIC Guardar dataframe final (Cruz8) en el contenedor (resultados)
// MAGIC """
// MAGIC 
// MAGIC output_container_path = "wasbs://%s@%s.blob.core.windows.net" % (container, storage_account_name)
// MAGIC output_blob_folder = "/mnt/parce/"
// MAGIC 
// MAGIC # escribir el marco de datos como un solo archivo en el almacenamiento
// MAGIC 
// MAGIC (Cruz8
// MAGIC  .coalesce(1)
// MAGIC  .write
// MAGIC  .mode("overwrite")
// MAGIC  .option("delimiter","|")
// MAGIC  .option("header", "true")
// MAGIC  .option("encoding", "ISO-8859-1")
// MAGIC  .format("com.databricks.spark.csv")
// MAGIC  .save(output_blob_folder))
// MAGIC 
// MAGIC # Obtenga el nombre del archivo CSV de datos manipulados que se acaba de guardar en el almacenamiento de blobs de Azure (comienza con "part-")
// MAGIC files = dbutils.fs.ls(output_blob_folder)
// MAGIC output_file = [x for x in files if x.name.startswith("part-")]
// MAGIC 
// MAGIC # Mover el archivo CSV de datos manipulados de una subcarpeta (carpeta de datos manipulados) a la raíz del contenedor
// MAGIC # Al mismo tiempo que se cambia el nombre del archivo
// MAGIC dbutils.fs.mv(output_file[0].path, "%s/01_Datos_etiquetados_21092021.csv" % output_container_path)
