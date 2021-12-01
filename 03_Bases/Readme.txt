-------------------------------------------------------------------------------------------------------
                                         Datos de muestra
-------------------------------------------------------------------------------------------------------


A continuación describimos los datos de muestra del proyecto, con el propósito de que pueda ser replicado de una forma básica.

   ***Nota: Compartimos insumos de muestra producidos por el código de etiquetado ejecutado en DataBricks, pero NO compartimos las bases de insumo para la                ejecución de dichos códigos. 


 1. Datos entrenados:
   
    Base de interacciones de usuarios con su respectiva etiqueta - respuesta

    Base: 01_Datos_entrenados_Muestra.xlsx
    Tamaño: 49 Kb
   

    Esta base es resultado del procesamiento de datos ejecutado en DataBricks, la base tiene los siguientes campos:

     - Usuario: Mensaje original del usuario.
     - Etiqueta: Tópico o tema asignado por el algoritmo de etiquetado.
     - Respuesta: Respuesta correspondiente al tópico o tema.


 2. Insumo de QnA Maker:
  
    Base de pares: Pregunta - Respuesta

    Base: 01_Datos_entrenados_Muestra_QnA.xlsx
    Tamaño: 45 Kb


    Esta base es resultado del procesamiento de datos ejecutado en DataBricks, la base tiene los siguientes campos:

     - Usuario: Mensaje original del usuario.
     - Respuesta: Respuesta correspondiente al tópico o tema.

     *** Esta base es una muestra del insumo usado para el servicio QnA Maker de PARCE.


 3. ChitChat para QnA de PARCE:

    ChitChat profesional para complemento de respuestas del servicio QnA de PARCE.

    Base: 02_qna_chitchat_professional_21072021.tsv

    *** Esta base corresponde al insumo usado para el servicio QnA Maker de PARCE.

 
 4. Diccionario para Storage

    Diccionario de tópicos definidos para PARCE en la estructura requerida por los notebook de Azure Synapse Analytics.

   
    Base: 05_Diccionario.csv
    Tamaño: 15 Kb

     *** Esta base corresponde al insumo usado para el servicio Azure Synapse Analytics de PARCE.