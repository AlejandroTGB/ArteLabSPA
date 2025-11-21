#  Artelab SPA - Android Inventory App

> Aplicación nativa Android moderna para la gestión y catálogo de productos artísticos, desarrollada con Kotlin y Jetpack Compose.

##  Sobre el Proyecto

**Artelab SPA** es una aplicación móvil diseñada para gestionar un inventario de materiales de arte. Permite a los usuarios visualizar un catálogo estilo "feed", y administrar productos mediante operaciones CRUD (Crear, Leer, Actualizar, Eliminar) completas.

El proyecto destaca por su implementación de una arquitectura limpia (MVVM), persistencia de datos local robusta y la integración fluida con recursos nativos del dispositivo como la cámara y la galería de imágenes.

---

##  Funcionalidades (Demo)

### 1. Navegación y Feed de Productos
Interfaz moderna basada en Material Design 3 con navegación inferior y scroll infinito. La UI reacciona automáticamente a los cambios en la base de datos.

![Demo del Feed y Navegación](screenshots/demo_feed.gif)
*(Visualización del Home Screen, scroll de productos y barra de navegación)*

### 2. Creación de Productos con Cámara
Formulario validado en tiempo real. Integración segura con la **Cámara** del dispositivo utilizando `FileProvider` para capturar imágenes de alta calidad.

![Demo Crear con Cámara](screenshots/demo_create.gif)
*(Flujo de agregar un nuevo producto, validación de campos y uso de la cámara)*

### 3. Edición y Selector de Galería
Reutilización del formulario para modo edición. Integración con el **Photo Picker** nativo de Android para seleccionar imágenes de la galería de forma segura y privada.

![Demo Editar con Galería](screenshots/demo_edit.gif)
*(Flujo de editar un producto existente y cambiar la foto desde la galería)*

---

##  Tech Stack & Librerías

El proyecto utiliza las últimas tecnologías y mejores prácticas de desarrollo Android moderno (2024-2025):

* **Lenguaje:** [Kotlin](https://kotlinlang.org/) (100%)
* **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material Design 3)
* **Arquitectura:** MVVM (Model-View-ViewModel) + Clean Architecture principles.
* **Persistencia Local:** [Room Database](https://developer.android.com/training/data-storage/room) (SQLite abstraction).
* **Concurrencia & Reactividad:** Kotlin Coroutines & [Flow](https://kotlinlang.org/docs/flow.html).
* **Navegación:** [Navigation Compose](https://developer.android.com/guide/navigation/navigation-compose) (con paso de argumentos).
* **Carga de Imágenes:** [Coil](https://coil-kt.github.io/coil/compose/).
* **Recursos Nativos:**
    * AndroidX Activity Result API (para permisos y resultados).
    * Photo Picker (PickVisualMedia).
    * Camera integration & FileProvider.

---

##  Estructura del Proyecto

El código fuente está organizado siguiendo los principios de **Clean Architecture** y **MVVM**, separando claramente las responsabilidades por capas:

```text
com.duroc.artelabspa
├── 📁 data.local           # Capa de Persistencia (Room Database)
│   ├── AppDatabase.kt      # Configuración abstracta de la Base de Datos.
│   ├── DatabaseProvider.kt # Singleton para garantizar una conexión única.
│   └── ProductoDao.kt      # Interfaz de consultas SQL (DAO).
│
├── 📁 model                # Capa de Datos Pura (Entities)
│   └── Producto.kt         # Data class que representa la tabla "productos".
│
├── 📁 repository           # Capa de Reglas de Negocio
│   ├── FormValidator.kt    # Lógica de validación (precios, vacíos, etc).
│   └── ProductoRepository.kt # Fuente única de verdad (Single Source of Truth).
│
├── 📁 ui                   # Capa de Presentación (Jetpack Compose)
│   ├── 📂 components
│   │   └── ProductCard.kt      # Componente visual reutilizable (Tarjeta).
│   ├── 📂 screens
│   │   ├── FormScreen.kt       # Pantalla de Formulario + Lógica de Cámara/Galería.
│   │   └── HomeScreen.kt       # Pantalla Principal (Feed) + Barra de Navegación.
│   ├── AppNavigation.kt    # Configuración del grafo de navegación (NavHost).
│   └── NavRoutes.kt        # Constantes de rutas (URLs internas).
│
└── 📁 viewmodel            # Capa de Gestión de Estado
    ├── FormViewModel.kt    # Gestiona el estado de escritura y guardado.
    ├── HomeViewModel.kt    # Gestiona el estado de lectura (Feed) y borrado.
    └── *Factory.kt         # Clases para inyectar el repositorio en los ViewModels.
---

*Evaluación de Programación de Aplicaciones Móviles - [2025]*
