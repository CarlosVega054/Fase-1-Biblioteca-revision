-- Base de datos simple para proyecto estudiantil
DROP DATABASE IF EXISTS bd_biblioteca;
CREATE DATABASE bd_biblioteca;
USE bd_biblioteca;

-- Tabla de Roles
CREATE TABLE roles (
    id_rol INT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

INSERT INTO roles VALUES (1, 'ADMINISTRADOR'), (2, 'PROFESOR'), (3, 'ALUMNO');

-- Tabla de Usuarios
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    apellidos VARCHAR(100),
    correo VARCHAR(100) UNIQUE,
    password VARCHAR(100),
    id_rol INT,
    FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
);

INSERT INTO usuarios (nombre, apellidos, correo, password, id_rol) 
VALUES ('Admin', 'Sistema', 'admin@donbosco.edu', 'admin123', 1);

-- Tabla Documentos general
CREATE TABLE documentos (
    id_documento INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) UNIQUE,
    titulo VARCHAR(200),
    autor VARCHAR(200),
    anio_publicacion INT,
    clasificacion VARCHAR(50),
    ubicacion VARCHAR(100),
    tipo VARCHAR(50), -- LIBRO, REVISTA, CD, TESIS
    disponibles INT,
    total INT,
    estado_fisico VARCHAR(50) DEFAULT 'DISPONIBLE' -- DISPONIBLE, PRESTADO, EN REPARACION, RESERVADO
);

-- Tablas especificas
CREATE TABLE libros (
    id_documento INT PRIMARY KEY,
    isbn VARCHAR(50),
    editorial VARCHAR(100),
    FOREIGN KEY (id_documento) REFERENCES documentos(id_documento) ON DELETE CASCADE
);

CREATE TABLE revistas (
    id_documento INT PRIMARY KEY,
    issn VARCHAR(50),
    edicion INT,
    FOREIGN KEY (id_documento) REFERENCES documentos(id_documento) ON DELETE CASCADE
);

CREATE TABLE cds (
    id_documento INT PRIMARY KEY,
    genero VARCHAR(50),
    duracion INT,
    FOREIGN KEY (id_documento) REFERENCES documentos(id_documento) ON DELETE CASCADE
);

CREATE TABLE tesis (
    id_documento INT PRIMARY KEY,
    carrera VARCHAR(100),
    universidad VARCHAR(100),
    FOREIGN KEY (id_documento) REFERENCES documentos(id_documento) ON DELETE CASCADE
);

-- Préstamos
CREATE TABLE prestamos (
    id_prestamo INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    id_documento INT,
    fecha_prestamo DATE,
    fecha_devolucion DATE,
    estado VARCHAR(50) DEFAULT 'PRESTADO', -- PRESTADO, DEVUELTO
    mora DECIMAL(10,2) DEFAULT 0.0,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_documento) REFERENCES documentos(id_documento)
);

-- ==========================================
-- DATOS DE PRUEBA (EJEMPLARES)
-- ==========================================

-- 1. Insertar Libros
INSERT INTO documentos (codigo, titulo, autor, anio_publicacion, clasificacion, ubicacion, tipo, disponibles, total, estado_fisico) VALUES 
('LIB-001', 'Cien años de soledad', 'Gabriel García Márquez', 1967, '863.44', 'Estante Literatura A-12', 'LIBRO', 4, 6, 'DISPONIBLE'),
('LIB-002', 'El Principito', 'Antoine de Saint-Exupéry', 1943, '843', 'Estante Literatura A-08', 'LIBRO', 9, 15, 'DISPONIBLE'),
('LIB-003', 'Don Quijote de la Mancha', 'Miguel de Cervantes', 1605, '863', 'Estante Literatura A-15', 'LIBRO', 3, 4, 'DISPONIBLE'),
('LIB-004', 'Biología Moderna', 'Solomon, Berg, Martin', 2021, '570', 'Estante Ciencias A-25', 'LIBRO', 18, 35, 'DISPONIBLE'),
('LIB-005', 'Matemáticas 2° Bachillerato', 'Ministerio de Educación', 2023, '510', 'Estante Matemáticas B-05', 'LIBRO', 22, 40, 'DISPONIBLE'),
('LIB-006', 'Física Universitaria', 'Sears & Zemansky', 2018, '530', 'Estante Física A-30', 'LIBRO', 12, 25, 'DISPONIBLE'),
('LIB-007', 'Química General', 'Raymond Chang', 2020, '540', 'Estante Química A-28', 'LIBRO', 15, 28, 'DISPONIBLE'),
('LIB-008', 'Historia de El Salvador', 'Ministerio de Educación', 2022, '972.84', 'Estante Historia B-12', 'LIBRO', 20, 30, 'DISPONIBLE'),
('LIB-009', 'Inglés para Bachillerato', 'Ministerio de Educación', 2024, '420', 'Estante Idiomas C-03', 'LIBRO', 30, 45, 'DISPONIBLE'),
('LIB-010', 'Introducción a la Programación con Java', 'Cay Horstmann', 2022, '005.133', 'Estante Informática D-10', 'LIBRO', 7, 18, 'DISPONIBLE');

INSERT INTO libros (id_documento, isbn, editorial) VALUES 
(1, '978-8437604947', 'Sudamericana'),
(2, '978-987-566-478-4', 'Reynal & Hitchcock'),
(3, 'S/I', 'Francisco de Robles'),
(4, '978-607-744-892-1', 'Cengage Learning'),
(5, 'S/I', 'MINED'),
(6, 'S/I', 'Pearson'),
(7, 'S/I', 'McGraw Hill'),
(8, 'S/I', 'MINED'),
(9, 'S/I', 'MINED'),
(10, 'S/I', 'Pearson');

-- 2. Insertar Tesis
INSERT INTO documentos (codigo, titulo, autor, anio_publicacion, clasificacion, ubicacion, tipo, disponibles, total, estado_fisico) VALUES 
('TES-001', 'Implementación de un sistema de biblioteca digital', 'Carlos Antonio Ramírez', 2024, '025.3', 'Archivo Tesis Gabinete C-01', 'TESIS', 2, 2, 'DISPONIBLE'),
('TES-002', 'Análisis de la deserción escolar en Soyapango', 'María José López', 2023, '371.29', 'Archivo Tesis Gabinete C-02', 'TESIS', 2, 2, 'DISPONIBLE'),
('TES-003', 'Impacto de redes sociales en rendimiento académico', 'José Miguel Santos', 2025, '302.23', 'Archivo Tesis Gabinete C-03', 'TESIS', 1, 1, 'DISPONIBLE');

INSERT INTO tesis (id_documento, carrera, universidad) VALUES 
(11, 'Ingeniería en Sistemas', 'Universidad Don Bosco'),
(12, 'Licenciatura en Educación', 'Universidad Nacional'),
(13, 'Psicología', 'Universidad Centroamericana');

-- 3. Insertar Revistas
INSERT INTO documentos (codigo, titulo, autor, anio_publicacion, clasificacion, ubicacion, tipo, disponibles, total, estado_fisico) VALUES 
('REV-001', 'National Geographic', 'National Geographic Society', 2025, '050', 'Estante Revistas B-20', 'REVISTA', 11, 15, 'DISPONIBLE'),
('REV-002', 'Revista ECA (Estudios Centroamericanos)', 'UCA', 2024, '050', 'Estante Revistas B-22', 'REVISTA', 5, 8, 'DISPONIBLE');

INSERT INTO revistas (id_documento, issn, edicion) VALUES 
(14, '0027-9358', 1),
(15, 'S/I', 1);

-- 4. Insertar CDs
INSERT INTO documentos (codigo, titulo, autor, anio_publicacion, clasificacion, ubicacion, tipo, disponibles, total, estado_fisico) VALUES 
('CD-001', 'Matemáticas Interactivas 1° Bachillerato', 'MINED', 2023, '004', 'Caja Multimedia D-05', 'CD', 8, 12, 'DISPONIBLE'),
('CD-002', 'Inglés Pronunciation Practice', 'Cambridge University Press', 2022, '420', 'Caja Multimedia D-06', 'CD', 6, 10, 'DISPONIBLE');

INSERT INTO cds (id_documento, genero, duracion) VALUES 
(16, 'Educativo', 60),
(17, 'Educativo', 120);
