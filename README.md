Análisis y documentación de la arquitectura MVVM

1.1. Documentación de los componentes (basada en MVVMSimple.kt)

La arquitectura MVVM (Model-View-ViewModel) divide una aplicación en tres componentes principales: Model, ViewModel y View. Cada uno tiene una responsabilidad diferente, lo que permite organizar mejor el código y separar la lógica de negocio de la interfaz gráfica.
Model (Modelo)
Su función principal es almacenar los datos de la aplicación. En este ejemplo guarda dos valores:
    • counter: almacena el número actual del contador.
    • message: almacena el mensaje que se muestra en pantalla.
El Model no contiene lógica para modificar la interfaz gráfica. Simplemente representa los datos y el estado de la aplicación.
ViewModel
El ViewModel está representado por la clase SimpleViewModel, que hereda de ViewModel.

Su función principal es gestionar el estado de la aplicación y actuar como intermediario entre el Model y la View.
Sus responsabilidades son:
    • Mantener el estado actual de la aplicación mediante mutableStateOf.
    • Modificar los datos cuando el usuario realiza alguna acción.
    • Actualizar el contador y el mensaje mediante el método incrementCounter().
    • Restaurar los valores iniciales mediante el método reset().
    • Exponer el estado a la interfaz mediante State<SimpleState>.

El estado interno _state es privado y modificable, mientras que state es público y permite consultar los datos sin modificarlos directamente desde la interfaz.

View (Vista / UI con Compose)
La View está formada por MainActivity y la función SimpleScreen().
MainActivity es la actividad principal de Android. Se encarga de iniciar la aplicación, crear el ViewModel y mostrar la interfaz utilizando setContent.
Por otro lado, SimpleScreen() construye la interfaz gráfica mediante Jetpack Compose.

Sus responsabilidades son:
    • Mostrar los datos que recibe del ViewModel.
    • Actualizar automáticamente la interfaz cuando cambia el estado.
    • Detectar las acciones del usuario, como pulsar un botón.
    • Comunicar esas acciones al ViewModel mediante sus funciones.

1.2. Análisis comparativo (MVVMSimple vs MVVM-separatModel)
1) Estructura y separación de responsabilidades

La principal diferencia entre los dos archivos está en cómo organizan el Model y dónde colocan la lógica de negocio.

En MVVMSimple.kt:
El Model está representado por una única clase llamada SimpleState, que solamente contiene los datos del contador y el mensaje.
La lógica para modificar estos datos está dentro del ViewModel, concretamente en las funciones incrementCounter() y reset().
Por tanto, el ViewModel se encarga tanto de gestionar el estado como de realizar las operaciones sobre los datos.

En MVVM-separatModel.kt:
El Model está separado en dos clases:
    • CardModel: representa una carta individual y contiene sus propiedades, como el identificador, la posición, la imagen y si está girada o emparejada.
    • BoardModel: representa el tablero completo y contiene la lista de cartas y el estado de la partida.
Además, BoardModel incluye la lógica de negocio mediante métodos como:
    • createInitialBoard(): crea el tablero inicial con seis cartas y tres parejas de imágenes.
    • onCardClick(position: Int): gira una carta según su posición.
    • checkMatches(): comprueba si dos cartas giradas tienen la misma imagen.

¿Dónde se ejecuta la lógica de negocio en cada archivo?

MVVMSimple.kt - Principalmente dentro del ViewModel (SimpleViewModel).
MVVM-separatModel.kt - Dentro del Model (BoardModel y CardModel).

En MVVM-separatModel.kt, el ViewModel se concentra en gestionar el estado, recibir las acciones de la interfaz y controlar las operaciones asíncronas, mientras que el Model contiene las reglas del juego.

2) Inmutabilidad y gestión del estado
En MVVM-separatModel.kt, los datos del tablero se modifican creando nuevas instancias de los objetos en lugar de cambiar directamente sus propiedades.
Para conseguirlo se utiliza el método .copy(), disponible en las clases declaradas como data class.

En este caso se crea un nuevo BoardModel con la lista de cartas actualizada, conservando el resto de propiedades.
¿Por qué es importante .copy()?
    • Permite mantener los objetos originales sin modificarlos directamente.
    • Facilita la creación de nuevos estados a partir de los anteriores.
    • Evita modificaciones inesperadas de los datos.
    • Permite que StateFlow detecte los nuevos valores y notifique a la interfaz de los cambios.
Esto es especialmente importante en una arquitectura reactiva, porque cuando el ViewModel publica un nuevo estado, Jetpack Compose puede detectar el cambio y actualizar la interfaz.

3) Ventajas de separar la lógica de negocio
Considero que separar la lógica de negocio (BoardModel) del ViewModel es mejor cuando una aplicación empieza a crecer.
Las principales ventajas son:
    • Mejor organización: cada clase tiene una responsabilidad concreta.
    • Reutilización: la lógica del juego puede utilizarse desde diferentes interfaces sin tener que duplicar código.
    • Facilidad de mantenimiento: si queremos cambiar las reglas del juego, podemos modificar el Model sin tener que cambiar toda la interfaz o el ViewModel.
    • Facilidad para realizar pruebas: podemos comprobar si las cartas se giran correctamente o si las parejas coinciden sin necesidad de iniciar la aplicación.
    • Menor dependencia: el Model no necesita conocer Jetpack Compose ni depender de la interfaz gráfica.
En conclusión, MVVM-separatModel.kt presenta una separación de responsabilidades más clara y una estructura más adecuada para aplicaciones grandes o con una lógica de negocio más compleja.

1.3. Análisis comparativo: StateFlow / MutableStateFlow y mutableStateOf / State
Estos mecanismos permiten gestionar el estado de una aplicación y notificar a la interfaz cuando los datos cambian. Sin embargo, tienen algunas diferencias en su funcionamiento y en el contexto en el que se utilizan.

1) StateFlow y MutableStateFlow

StateFlow es un mecanismo de Kotlin Coroutines que permite almacenar y observar un estado que puede cambiar con el tiempo.
    • MutableStateFlow: permite modificar el valor del estado.
    • StateFlow: permite consultar y observar el estado, pero no modificarlo directamente.
      
En MVVM-separatModel.kt:
    • _board es privado y permite modificar el estado del tablero.
    • board es público y permite observar los cambios.
    • asStateFlow() expone el estado como un flujo de solo lectura.
Para utilizarlo en Jetpack Compose se necesita collectAsState():
Esta función recoge los valores emitidos por StateFlow y los convierte en un estado observable por Compose. Cuando cambia el tablero, la interfaz se actualiza automáticamente.

2) mutableStateOf y State
mutableStateOf es una herramienta de Jetpack Compose que permite crear un estado observable por la interfaz.
    • mutableStateOf: crea un estado que se puede modificar.
    • State: permite consultar ese estado sin modificarlo.

En MVVMSimple.kt:
    • _state es privado y permite modificar los datos.
    • state es público y permite acceder a ellos.
Cuando cambia _state.value, Compose detecta el cambio y actualiza los elementos de la interfaz que dependen de ese estado.
      
A diferencia de StateFlow, no es necesario utilizar collectAsState(), porque mutableStateOf ya está integrado con el sistema de estados de Jetpack Compose.

3) Diferencias entre ambos mecanismos
StateFlow y MutableStateFlow pertenecen a Kotlin Coroutines, mientras que State y mutableStateOf forman parte de Jetpack Compose.
En cuanto a la modificación del estado, MutableStateFlow permite cambiar los valores almacenados, mientras que StateFlow permite únicamente observarlos. De forma similar, mutableStateOf permite modificar el estado y State permite acceder a él sin modificarlo.
Otra diferencia importante está en la actualización de la interfaz. Cuando utilizamos StateFlow en Jetpack Compose, necesitamos collectAsState() para recoger los cambios y convertirlos en un estado observable por Compose. En cambio, mutableStateOf está integrado directamente con Jetpack Compose, por lo que la interfaz se actualiza automáticamente cuando cambia el valor observado.
Respecto a su uso principal, StateFlow está pensado para gestionar estados reactivos de forma independiente de la interfaz gráfica, mientras que mutableStateOf se utiliza principalmente para gestionar estados relacionados con la interfaz de usuario.
Por último, StateFlow está integrado con Kotlin Coroutines y permite trabajar con operaciones asíncronas. Por otro lado, mutableStateOf no necesita utilizar corrutinas para funcionar, ya que forma parte del sistema de estados de Jetpack Compose.

4) ¿Cuál elegiría para esta aplicación y por qué?
Para desarrollar el juego Tres en Raya elegiría StateFlow y MutableStateFlow.
La razón principal es que quiero mantener una separación clara entre la lógica de negocio, el ViewModel y la interfaz gráfica.
Con MutableStateFlow, el ViewModel puede gestionar el estado del tablero y publicar sus cambios mediante StateFlow, mientras que la interfaz utiliza collectAsState() para recibir las actualizaciones.
Además, StateFlow permite trabajar fácilmente con Kotlin Coroutines y operaciones asíncronas.
Por ejemplo, en el archivo MVVM-separatModel.kt se utiliza viewModelScope.launch junto con delay() para esperar un segundo antes de comprobar si las dos cartas giradas coinciden.
Esto demuestra que StateFlow se integra bien con las corrutinas y permite gestionar estados de una manera reactiva.
Por estos motivos, considero que StateFlow es una buena elección para el proyecto Tres en Raya, ya que permite mantener una arquitectura organizada, reactiva y fácil de ampliar.
