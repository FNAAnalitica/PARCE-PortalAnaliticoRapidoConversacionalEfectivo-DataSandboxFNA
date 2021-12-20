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

![ChatBot](04_Resultados/Captura.JPG)
