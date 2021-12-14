# Procesamiento en DataBricks

Códigos de intersección de bolsas de N-gramas para etiquetado de mensajes de usuario


1. **01_Etiquetador_UDF**
    
    Desarrollo en pyspark para el aprovechamiento de procesamiento cloud de DataBricks. El srcipt usa funciones UDF para hacer alrededor de 14 millones de
    intersecciones de listas de N-gramas, para después elegir los mejores rankeos y así etiquetar cada mensaje de usuario con un tópico del diccionario.
    

2. **02_Etiquetador_Top**

    Desarrollo en pyspark para el aprovechamiento de procesamiento cloud de DataBricks. El srcipt usa funciones UDF para hacer alrededor de 14 millones de
    intersecciones de listas de N-gramas, para después elegir los mejores rankeos con la opción de seleccionar top de afinidades con base en un umbral, y 
    así etiquetar cada mensaje de usuario con un tópico del diccionario.


- Lenguaje: Pyspark
- Formatos: 
   - .dbc 
   - .scala 
   - .HTML
