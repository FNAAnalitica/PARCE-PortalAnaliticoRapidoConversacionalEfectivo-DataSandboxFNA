# ¿Qué es PARCE?

El Portal Analítico Rápido, Conversacional y Efectivo (PARCE) comprende un conjunto de herramientas en función de experiencias conversacionales para usuarios y empresas, es decir, PARCE brinda una asistencia conversacional a los usuarios y además brinda un ambiente analítico de la información conversacional a la empresa.

En detalle, PARCE está articulado por 3 pilares:

1. **Asistente conversacional**: Es el servicio conversacional de PARCE, un servicio cognitivo entrenado para responder a las necesidades de los usuarios (preguntas del usuario) a través de un chatbot.
2. **Almacenamiento de información**: PARCE tiene su propia bodega de datos, en este repositorio se guarda la información de las métricas asociadas a las interacciones que hace el usuario con el asistente conversacional.
3. **Ambiente analítico**: El ambiente analítico de PARCE consta de un espacio que usa los datos almacenados para desarrollar tableros de visualización y modelos analíticos para que la empresa pueda usar los datos que genera el asistente conversacional con el usuario.

# ¿Qué queremos con PARCE?

Con el desarrollo de PARCE pretendemos explorar las funcionalidades y alcance de los servicios *cloud* en pro del desarrollo de proyectos de Big Data y a la transformación digital de las entidades públicas.

PARCE integra habilidades de inteligencia artificial, almacenamiento de datos y desarrollo analítico, encaminadas a los siguientes objetivos:

1. **Experiencia de usuario**: A través del desarrollo de PARCE explorar habilidades de IA para definir experiencias cognitivas dirigidas a la atención del usuario.
2. **Agilidad analítica**: Diseñar y desplegar un ambiente de gestión de datos y desarrollo analítico para que la empresa tenga la facilidad de explorar y describir la información conversacional de los usuarios.
3. **AI no tercerizada**: Explorar, investigar y documentar los servicios de tecnológicos de nube para establecer modelos de IA inhouse.


# Contenido

En este repositorio almacena los elementos básicos para replicar de forma sencilla y preliminar el proyecto PARCE.

1. **Códigos**: Script del proyectos, referentes a la etapa de procesamiento y analítica de los datos captados por PARCE.

   - SDK: Script que define el funcionamiento interno del Bot.
   - DataBricks: Script para el procesamiento y generación de los datos de entrenamiento para QnA Maker.
   - AzureSynapse: Notebook para la ingesta de los datos captados por el bot, y generación de insumos para modelos analíticos y de visualización.
   - PBI: Tablero de visualización.
   - Bot: HTML para el uso y manipulación del chatbot.
   
2. **Documentación**: Hitos y documentación base para el entendimiento del potencial uso de los servicios Azure en el desarrollo de PARCE.

   - Enlaces documentales.
   - Guía de Microsoft para el diseño de chatbot en pro de la experiencia conversacional de los usuarios.
   
3. **Bases**: Bases de datos básicas para la ejecución simple del proyecto.

   - Muestra de datos producidos por el algoritmo de etiquetado (Ejecutado en DataBricks).
   - Muestra de datos producidos por el algoritmo de etiquetado, adecuado para su uso en QnA Maker.
   - Chitchat como complemento de las KB de QnA Maker para PARCE.
   - Diccionario de tópicos, como insumo para la ingesta de datos en Azure Synapse.
   
4. **Resultados**:
