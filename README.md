# Proyecto de Biblioteca - Colegio Amigos de Don Bosco

Este es el sistema que estamos trabajando para la biblioteca del colegio. Básicamente, sirve para llevar el control de los libros, revistas, CDs y tesis, además de manejar los préstamos de los alumnos y profes.

---

## Cómo está organizado el código

Para que no nos perdamos, el proyecto está dividido en paquetes según lo que hace cada parte:

*   **src/modelo:** Aquí están las clases básicas como `Usuario`, `Libro`, `Prestamo`, etc. Son solo los datos.
*   **src/dao:** Aquí está toda la lógica de la base de datos (MySQL). Si necesitan cambiar una consulta o ver cómo se guarda algo, es aquí.
*   **src/vista:** Todas las ventanas (Frames) que hicimos con Swing.
*   **src/utilidades:** Cosas extra como el manejo de errores y formatos de fecha.
*   **database:** Aquí dejé el archivo `.sql` para que puedan crear la base de datos en sus compus.

---

## Reglas de los Préstamos (Importante)

El sistema ya tiene validados los límites que nos pidieron:

1.  **Administradores:** Pueden hacer de todo y no tienen límites de préstamos.
2.  **Profesores:** Pueden sacar hasta **5 cosas** a la vez y tienen **15 días** para devolverlas.
3.  **Alumnos:** Pueden sacar hasta **3 cosas** y tienen solo **7 días**.

**Ojo con la Mora:** El sistema cobra **$0.50 por cada día de retraso**. Si alguien debe dinero o tiene libros vencidos, el programa no lo va a dejar sacar nada más hasta que se ponga al día.

---

## Pasos para que les funcione (Instalación)

Si se bajaron el proyecto y no les corre, revisen esto:

1.  **La Base de Datos:** Entren a su MySQL y corran el script que está en `database/BD_Colegio.sql`.
2.  **Conexión:** Chequen el archivo `src/dao/Conexion.java` y asegúrense de que el usuario y la contraseña de MySQL sean los mismos que ustedes usan.
3.  **El Driver:** No se olviden de agregar el `mysql-connector` a las librerías del proyecto en el IDE, si no, va a dar error de conexión.
4.  **Para correrlo:** La clase principal es `src/Principal.java`.

---

## Cuentas para probar

Para entrar por primera vez sin tener que crear un usuario, usen esta:

*   **Usuario:** `admin@donbosco.edu`
*   **Contraseña:** `admin123`

Cualquier duda con el código, me avisan.
