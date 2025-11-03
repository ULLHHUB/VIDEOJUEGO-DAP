# Simulador de Batallas Estratégicas

Este proyecto es un simulador de batallas estratégicas desarrollado en Java con JavaFX. Permite a los jugadores crear ejércitos de diferentes civilizaciones, cada una con unidades únicas, y enfrentarlos en diversos mapas que afectan las estadísticas de combate.

## 🚀 Características

*   **Múltiples Civilizaciones**: Elige entre Romanos, Vikingos y Egipcios, cada uno con sus propias unidades.
*   **Tipos de Unidades**:
    *   **Soldado**: Fuerte en el combate cuerpo a cuerpo.
    *   **Arquero**: Ataca a distancia.
    *   **Lancero**: Especialista en defensa y cargas.
*   **Sistema de Ventajas y Desventajas**: Las unidades tienen ventajas sobre otras en un sistema tipo "piedra, papel o tijera":
    *   Soldado > Arquero
    *   Arquero > Lancero
    *   Lancero > Soldado
*   **Efectos del Terreno**: Lucha en diferentes mapas (Bosque, Montaña, Desierto) que otorgan bonificaciones o penalizaciones a ciertas unidades.
*   **Modos de Batalla**:
    *   **1v1**: Un duelo rápido entre dos unidades.
    *   **Batalla Masiva**: Un enfrentamiento a gran escala entre dos ejércitos.
*   **Feedback Visual Avanzado**:
    *   Animaciones de ataque y muerte.
    *   Efectos de brillo para golpes críticos y ventajas de tipo.
    *   Barras de vida dinámicas.
*   **Gestión de Sonido**: Música de fondo y efectos de sonido para una experiencia más inmersiva.

## 🛠️ Patrones de Diseño Implementados

El proyecto está estructurado siguiendo varios patrones de diseño de software clave:

*   **Abstract Factory**: `CivilizacionFactory` se utiliza para crear familias de unidades (Soldado, Arquero, Lancero) para cada civilización sin especificar sus clases concretas.
*   **Strategy**: `EstrategiaAtaque` permite definir diferentes algoritmos de ataque (cuerpo a cuerpo, a distancia) e intercambiarlos dinámicamente.
*   **Singleton**: `GestorJuego` y `GestorSonido` aseguran que solo exista una instancia de estos gestores globales, proporcionando un punto de acceso único a la lógica del juego y a los recursos de sonido.
*   **Template Method**: `BatallaTemplate` define el esqueleto de una batalla, permitiendo que las subclases (`Batalla1v1`, `BatallaMasiva`) redefinan pasos específicos del algoritmo sin cambiar la estructura general de la batalla.
*   **Model-View-Controller (MVC)**: Aunque no es un patrón GoF estricto, la separación entre la lógica del modelo (`Unidad`), la vista (`UnidadVisual`, `MainView.fxml`) y el controlador (`MainController`) es fundamental en la arquitectura.

## 💻 Tecnologías Utilizadas

*   **Java 11**
*   **JavaFX 17.0.2**: Para la interfaz gráfica de usuario.
*   **Maven**: Para la gestión de dependencias y la construcción del proyecto.

## ⚙️ Cómo Ejecutar el Proyecto

1.  **Clonar el repositorio**:
    ```bash
    git clone https://github.com/ULLHHUB/VIDEOJUEGO-DAP.git
    ```
2.  **Abrir el proyecto** en tu IDE preferido (IntelliJ IDEA, VS Code con soporte para Java, etc.).
3.  **Asegúrate de tener Maven configurado** y que el IDE descargue las dependencias del archivo `pom.xml`.
4.  **Ejecutar la aplicación** a través de la clase `MainApp.java` o utilizando el plugin de Maven para JavaFX:
    ```bash
    mvn clean javafx:run
    ```

## 📂 Estructura del Proyecto

El código fuente se organiza en los siguientes paquetes principales:

*   `com.ull.dap.juego.control`: Contiene las clases Singleton que gestionan el estado global del juego (`GestorJuego`, `GestorSonido`).
*   `com.ull.dap.juego.model`:
    *   `batalla`: Implementaciones del patrón Template Method para los diferentes modos de batalla.
    *   `estrategia`: Implementaciones del patrón Strategy para los tipos de ataque.
    *   `factoria`: Implementaciones del patrón Abstract Factory para cada civilización.
    *   `unidades`: Clases que representan los modelos de datos de las unidades.
*   `com.ull.dap.juego.view`: Contiene las clases de la interfaz de usuario, incluyendo el `MainController`, la clase `MainApp` y las representaciones visuales como `UnidadVisual`.
*   `resources`:
    *   `com/ull/dap/juego/view`: Contiene el archivo `MainView.fxml`.
    *   `images`: Imágenes y sprites de las unidades.
    *   `sounds`: Archivos de audio para la música y los efectos de sonido.
