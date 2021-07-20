## productListProof

#### Realicé una primera version con SpringBoot Mvc y RestTemplate pero intentando hacer llamadas simulaneas vi que podria ser mejor opción la programacion reactiva.
#### Los objetivos no eran demasiado concretos por lo que al final he hecho algunas consideraciones que si lo desean puedo cambiar.

- He visto que contenia ciertos sleeps en algunos de los productsIds, algunos con 5 seg y otros con 50, por lo que he decidido que si tarda mas de 6 segundos la llamada se considera una llamada al producto invalida (lo he dejado como propiedad para que pueda editarse facilmente si los requerimientos son otros).
- En el caso que la llamada a un producto falle continua con el resto de llamadas al resto de productos (en muchos casos de uso esto no tendria sentido pero en casos de recomendaciones de prendas pensé que tiene mas sentido que si puede dar 2 productos similares al menos eso es mejor que retornar un error y no hacer recomendaciones), de todas formas esto es perfectamente editable y en un caso de uso real se definiria en la tarea en si.
- He dejado una politica de excepciones y errores bastante simple porque normalmente es algo que prefiero que sea discutido por el equipo de desarrollo o aplicar la politica vigente en el proyecto.
- Respecto a los test he creado unitarios para casos de uso sencillos, si desean alguno mas no duden en comentarme.
