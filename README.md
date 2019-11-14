# Desarrollando una app Spring Boot con GraphQL

![Desarrollando una app Spring Boot con GraphQL](https://ktufernando.github.io/static/c55003e547393b53aee53fdfc17d67dd/071a8/1.webp)

¿Graph Q qué? Quizá no tenes idea que es pero seguro escuchaste o leíste por algún lado el nombre. Hace un tiempo escribí artículos sobre [API](https://medium.com/@ktufernando/api-roquefort-y-nuez-80c553f2dc1c) y sobre [API Rest](https://medium.com/@ktufernando/api-rest-api-qu%C3%A9-4ce8630da829). Fernando ¿Y que tiene que ver GraphQL con una API? Es verdad, el nombre suena más a una base de datos de grafo que a una tecnología para desarrollar APIs.

Posiblemente escuchaste sobre GraphQL y cómo Facebook usa GraphQL en sus aplicaciones móviles. En este artículo, voy a contarte qué es y cómo implementar GraphQL en una aplicación Spring Boot. Así vas a poder saber que magia ofrece GraphQL.

### ¿Por qué GraphQL?

Si no sabes sobre GraphQL, entonces estás leyendo el artículo correcto. GraphQL es un lenguaje de consulta en tu API. Por más que query te haga pensar en una base de datos, GraphQL no está vinculado a ninguna base de datos o motor de almacenamiento específico. En cambio, GraphQL se apoya en tu código y datos existentes.

![GraphQL](./2.png "GraphQL")

No entendí nada, explícame de nuevo Fernando.

Desarrollar una API con GraphQL da la capacidad de que al consumirla podemos realizar queries sobre nuestros datos a través de un solo endpoint.

#### La principal ventaja de usar GraphQL es:

No es necesario crear múltiples endpoints de nuestra API en una aplicación, a diferencia de REST, donde exponemos múltiples endpoints para recuperar datos como este.

    https://tudominio.com/persona
    https://tudominio.com/persona/{id}

Usando GraphQL, obtenemos solo los datos que necesitamos o solicitamos. Esto es diferente en una implementación REST, cuando realizamos un request HTTP GET para obtener una respuesta JSON, obtenemos todos los datos incluso si queremos solo algunos atributos. Por ejemplo, cuando consultamos una API REST, obtenemos la respuesta completa en formato JSON como se muestra a continuación, incluso si solo necesitamos el id y el nombre.

    {"id": "100", "name": "Fernando", "age":36, "city": "Buenos Aires", "gender": "Male"}

La integración de aplicaciones front-end (como las aplicaciones móviles) con GraphQL son más rápidas y responsivas sobre las API REST.

Acá vamos a ver cómo construir una aplicación Spring Boot para persistir películas. Luego en esta misma app consultaremos películas usando GraphQL.

> Nota: El código fuente completo de este tutorial está disponible en mi cuenta de [GitHub](https://github.com/ktufernando). Podés descargarlo directamente del [repositorio](https://github.com/ktufernando/springboot-with-graphql) para poder ver el código completo. En este artículo me voy a centrar en la explicación de las clases/archivos claves.

Comencemos a desarrollar nuestra aplicación Spring Boot con GraphQL. Estoy usando IntelliJ IDEA. Sin embargo, podés usar cualquier IDE de tu elección.

### Crear la aplicación

Visitá [Spring Initializr](https://start.spring.io/) para generar una aplicación Spring Boot. Podes dejar las configuraciones principales por defecto. Proyecto maven, Spring Boot 2.2.1 y JDK 8. En el sector de dependencias vas a seleccionar: Spring Web, Spring Data JPA e HyperSQL Database.

![Spring Initializr](./3.jpg "Spring Initializr")

![Spring Initializr](./4.jpg "Spring Initializr")

Luego de descargar el zip, descomprimir e importar en nuestro IDE favorito, veamos el pom.xml. Las dependencias que necesitamos en nuestro pom son:

![pom.xml](./5.jpg "pom.xml")

> Nota: En esta imagen hay tres dependencias que tenemos que agregar después de descomprimir el zip de Spring Initializr.

> Las dos de graphql y la de lombok (solo para no tener que pegar tanto código en este post).

> El código lo podés copiar y pegar desde el repo de Github.

### Tarea 1: Clase de modelo

A continuación, agregaremos una clase de modelo para representar una película. La llamaremos Movie. El código de la clase de modelo es este.

![modelo](./6.jpg "modelo")

### Tarea 2: Repositorio de películas

El repositorio de este ejemplo extiende de JpaRepository.

![repositorio](./7.jpg "repositorio")

### Tarea 3: Agregar un esquema GraphQL

A continuación, escribiremos un esquema GraphQL, llamado schema.graphql en nuestra carpeta de resources.

![schema](./8.jpg "schema")

Este es un archivo muy importante y es la columna vertebral de GraphQL. Acá, definimos un esquema, que se relaciona con una query. También necesitamos indicar el tipo de query que usarán las aplicaciones front-end que consumirán nuestra api.

En este ejemplo, hay dos tipos de query:

    1. Cuando un usuario consulte todas los películas (utilizando *allBooks*), la api devolverá una array de películas.
    2. Cuando un usuario consulte una película específica al pasar el id, la api devolverá un objeto Película.

### Tarea 4: Crear los recopiladores de datos

Cada tipo de query en el esquema GraphQL tiene un buscador de datos correspondiente.

Necesitamos escribir dos clases de recopilación de datos, una para el tipo “allMovies” y otra para el tipo “movie” que definimos en el esquema.

La clase de recopilación de datos para el tipo allBooks es la siguiente.

![allMoviesFletcher](./9.jpg "allMoviesFletcher")

La clase de recopilación de datos para el tipo de movie es la siguiente.

![movieFletcher](./10.jpg "movieFletcher")

### Tarea 5: Proveedor de servicio GraphQL

Ahora, necesitamos agregar un servicio GraphQL. Vamos a nombrarlo como GraphQLProvider.

![graphQLProvider](./11.jpg "graphQLProvider")

Cuando se ejecuta la aplicación Spring Boot, Spring Framework llama al método @PostConstruct. El código dentro del método @PostConstruct hará tres cosas:

    1. Persistirá dos películas en la tabla Movie de la base de datos HQL.
    2. Creará la instancia de GraphQL que podremos inyectar donde la queramos usar.
    3. Buscadores de datos: allMovies y movie. Los nombres allMovies y movies definidos aquí deben coincidir con los tipos de query definidos en el archivo GraphQL que ya hemos creado.

### Tarea 6: Última tarea pero no menos importante. Endpoint de la API

Vamos a crear la clase MovieController y agreguemos un controlador de request POST, como este.

![controller](./12.jpg "controller")

Ahora si! A ejecutar la aplicación.

    mvn spring-boot:run

Con esto, nuestra aplicación Spring Boot GraphQL está lista. Ejecutemos nuestra aplicación Spring Boot y probémosla con la herramienta Postman.

#### Tenemos un único endpoint:

    http://localhost:8080/api/movies

Vamos a consultar varios conjuntos de datos con este único endpoint. Para hacerlo, abrí el Postman y hagamos las siguientes peticiones.

#### Request 1

Vamos a buscar una película específica cuyo id es “1001” y solo queremos el título en su respuesta. Y además en la misma petición, queremos todas las películas y esperamos que la respuesta contenga id, title, directors, actors y releaseDate.

Body del request:

    {
        movie(id:”1001") {
            title
        }
        allMovies {
            id
            title
            directors
            actors
            releaseDate
        }
    }

![postman](./13.jpg "postman")

*Response 1:* la respuesta para ambas consultas es esta.

    {
        movie(id:”1001") {
            title
        }
        allMovies {
            id
            title
            directors
            actors
            releaseDate
        }
    }

    {    
        "data": {
            "movie": {
                "title": "Guason"
            },
            "allMovies": [
                {
                    "id": "1001",
                    "title": "Guason",
                    "directors": [ 
                        "Todd Phillips"
                    ],
                    "actors": [
                        "Joaquin Phoenix",
                        "Robert De Niro"
                    ],
                    "releaseDate": "3 de octubre de 2019"
                },
                {
                    "id": "1002",
                    "title": "Avengers: Endgame",
                    "directors": [
                        "Anthony Russo",
                        "Joe Russo"
                    ],
                    "actors": [
                        "Robert Downey Jr.",
                        "Scarlett Johansson",
                        "Chris Evans"
                    ],
                    "releaseDate": "22 de abril de 2019"
                }
            ]
        },   
        "errors": [],   
        "dataPresent": true,
        "extensions": null
    }

#### Request 2

Volvé a consultar pero esta vez solo title y actors de una película específica por ID.

    { 
        movie(id:”1001"){
            title
            actors
        }
    }

*Response 2:* La salida es esta. Obtenemos el título y los actores de la película cuyo id es “1001”.

    {   
        "data": {
            "movie": {
                "title": "Guason",
                "actors": [
                    "Joaquin Phoenix",
                    "Robert De Niro"
                ]
            }
        },
        "errors": [],
        "dataPresent": true,
        "extensions": null
    }

#### Request 3

Ahora consultá todos los libros pero obteniendo solo el titulo, los directores y la fecha de publicación.

    {
        allMovies{
            title
            directors
            releaseDate
        }
    }

*Response 3:* La salida es esta.

    {
        "data": {
            "allMovies": [
                {
                    "title": "Guason",
                    "directors": [
                        "Todd Phillips"
                    ],
                    "releaseDate": "3 de octubre de 2019"
                },
                {
                    "title": "Avengers: Endgame",
                    "directors": [
                        "Anthony Russo",
                        "Joe Russo"
                    ],
                    "releaseDate": "22 de abril de 2019"
                }
            ]
        },
        "errors": [],
        "dataPresent": true,
        "extensions": null
    }

Entonces, acá esta la belleza de usar GraphQL sobre una API. Acá obtenemos exactamente lo que estamos buscando y no todo el conjunto completo de atributos en las respuestas JSON.

Puede descargar el código fuente completo de esta publicación desde [GitHub](https://github.com/ktufernando/springboot-with-graphql).

***

Muchas cosas de las que leíste en este artículo ya las sabés. La mayoría de este conocimiento que te compartí ya lo sabés. Sabemos lo que tenemos que hacer, sabemos lo que tenemos que evitar, todo esto ya lo sabemos. El único problema es que no lo ponemos en práctica, por esto es que necesito que te comprometas conmigo, en que si una de las ideas que mencioné resuena en vos, te interesa ponerla en práctica, que te comprometas a que vas a empezar hoy mismo con el paso más pequeño posible, el gesto más mínimo a hacerlo.

Solo pensar en poner en práctica no sirve, tenés que ponerte en práctica para tu crecimiento exponencial.

***

Antes de que te vayas…

¿Encontraste interesante el artículo? ¿Te gustaría que escriba sobre algún tema en particular?
Escribime o contactame a través de [Medium](https://medium.com/@ktufernando) o [GitHub](https://github.com/ktufernando) o [LinkedIn](https://www.linkedin.com/in/fervaldes/).