# Sistema de Biblioteca - Colegio Amigos de Don Bosco

## Roles y Restricciones de Préstamo

El sistema gestiona tres roles con permisos y límites de préstamos específicos:

### 1. Administrador (Rol 1)
* **Permisos:** Gestión integral del sistema, inventario y control de usuarios.
* **Préstamos:** Sin límite en la cantidad de préstamos activos ni límites de tiempo estrictos.

### 2. Profesor (Rol 2)
* **Límite de cantidad:** 5 préstamos simultáneos máximo.
* **Límite de tiempo:** 15 días de plazo para devolución.

### 3. Alumno / Estudiante (Rol 3)
* **Límite de cantidad:** 3 préstamos simultáneos máximo.
* **Límite de tiempo:** 7 días de plazo para devolución.

## Reglas de Mora

* **Tarifa Diaria:** Se cobra $0.50 por cada día de retraso posterior a la fecha de devolución.
* **Bloqueo por Morosidad:** Todo usuario con libros vencidos no podrá realizar nuevos préstamos hasta que el material sea devuelto y se salde la deuda acumulada.

## Credenciales de Acceso

El sistema cuenta con un usuario por defecto configurado para el rol de **Administrador**:
* **Correo / Usuario:** `admin@donbosco.edu`
* **Contraseña:** `admin123`

## Módulos Integrados

* **Módulo de Usuarios y Seguridad:** Control de acceso (Login) y gestión (CRUD) de usuarios. (`LoginFrame`, `GestionUsuariosFrame`, `UsuarioDAO`).
* **Módulo de Préstamos y Validaciones:** Interfaz y lógica para realizar préstamos y devoluciones con validación de mora y límites por rol. (`PrestamosFrame`, `PrestamoDAO`).
* **Base de Datos:** Script SQL de creación de tablas y datos de prueba (`database/BD_Colegio.sql`).
