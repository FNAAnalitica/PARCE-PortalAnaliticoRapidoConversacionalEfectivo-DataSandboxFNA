# Resultados
Resultados de la participación del **Fondo Nacional del Ahorro** en el Data Sandbox con el proyecto PARCE: *Portal Analítico Rápido Conversacional y Efectivo*.

A continuación detallamos los resultados del desarrollo de **PARCE** en *4 hitos* principales: 

## Procesamiento
Uno de los resultados más *notables* en la ejecución de PARCE es la **capacidad y velocidad de procesamiento**. A saber, en PARCE era necesario hacer *14'880.000* de iteraciones en una tarea para la creación de *datos de entrenamiento*, dicha tarea en ensayos previos en maquinas locales tardaría cerca de **62.000 horas** (aprox. **7 años**).  

Con códigos transcritos a *Pyspark* para el uso de *procesamiento distribuido* en **DataBricks** la tarea que antes tardaría *7 años*, con los servicios de *Azure* tardó **5 MINUTOS**.

## IA conversacional
Con nuestros propios datos de entrenamiento creamos *2 servicios cognitivos* que se comportan acorde a los datos con los que se entrenaron. 

1. QnA Maker que **RESPONDE** a las interacciones de los usuarios de PARCE. 
2. LUIS que **PREDICE** la intención del usuario con base en su mensaje.

<p align="center">
  <img src="/04_Resultados/Chatbot.JPG" />
</p>

Este servicio cognitivo (*QnA Maker*) conforma el **chatbot** de PARCE, este chatbot *responde a preguntas frecuentes* acerca de los temas o tópicos con los que entrenamos el servicio.

## Almacenamiento de información
Con el servicio de almacenamiento de **Cosmos DB** logramos conectar el chatbot a una ambiente de almacenamiento propio, el cual regitra una a una las interecciones del bot con usuarios, junto con métricas conversacionales.

<p align="center">
  <img src="/04_Resultados/Captura2.JPG" />
</p>

## Ambiente analítico
Con una captación de información conversacional ya estructurada y funcional, creamos un ambiente de ingesta de dicha información. Con Azure Synapse Analytics conformamos un ambiente en donde *transformamos y enriquecemos las métricas de la información conversacional del chatbot, para así disponer los datos conversacionales de PARCE para modelos
analíticos y en particular, para *tableros de visualización en Power BI*.

<p align="center">
  <img src="/04_Resultados/Tablero.JPG" />
</p>

En el tablero de visualización podemos identificar:

1. Conteo de mensajes.
2. Top de intenciones de usuarios y top de servicios.
3. Serie de tiempo de mensajes vs fallas de respuesta del bot (*línea gris*).
4. Nivel de respuesta de **QnA Maker**.
5. Nivel de predicción de **LUIS** con score promedio de predicción.
6. Comparativo de intenciones percibidas por **QnA Maker** y **LUIS**. 
