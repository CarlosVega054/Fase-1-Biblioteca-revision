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

-- 5. Nuevos Libros Agregados (Desde Imagen)
INSERT INTO documentos (id_documento, codigo, titulo, autor, anio_publicacion, clasificacion, ubicacion, tipo, disponibles, total, estado_fisico) VALUES 
(149, 'LIB-149', 'Así habló Zaratustra', 'Nietzsche', 2024, 'Filosofía', 'D6A', 'LIBRO', 1, 1, 'DISPONIBLE'),
(157, 'LIB-157', 'Becker - Economía del crimen', 'Becker', 2024, 'Economía', 'D4F', 'LIBRO', 4, 4, 'DISPONIBLE'),
(133, 'LIB-133', 'Biología Molecular', 'Watson', 2024, 'Biología', 'A4A', 'LIBRO', 3, 3, 'DISPONIBLE'),
(138, 'LIB-138', 'Brown', 'Química', 2024, 'Química', 'C2B', 'LIBRO', 2, 2, 'DISPONIBLE'),
(127, 'LIB-127', 'Cálculo Diferencial', 'Stewart', 2024, 'Matemáticas', 'C1F', 'LIBRO', 6, 6, 'DISPONIBLE'),
(156, 'LIB-156', 'Capitalismo y libertad', 'Friedman', 2024, 'Economía', 'C3C', 'LIBRO', 3, 3, 'DISPONIBLE'),
(113, 'LIB-113', 'Cervantes', 'Literatura', 2024, 'Literatura', 'A2F', 'LIBRO', 2, 2, 'DISPONIBLE'),
(101, 'LIB-101', 'Cormen', 'Informática', 2024, 'Informática', 'A1B', 'LIBRO', 3, 3, 'DISPONIBLE'),
(160, 'LIB-160', 'Cosmos', 'Sagan', 2024, 'Ciencia', 'B5F', 'LIBRO', 5, 5, 'DISPONIBLE'),
(110, 'LIB-110', 'Crítica de la Razón Pura', 'Kant', 2024, 'Filosofía', 'D3B', 'LIBRO', 4, 4, 'DISPONIBLE'),
(131, 'LIB-131', 'Deitel', 'Informática', 2024, 'Informática', 'D3E', 'LIBRO', 5, 5, 'DISPONIBLE'),
(129, 'LIB-129', 'Diamond', 'Historia', 2024, 'Historia', 'B6C', 'LIBRO', 4, 4, 'DISPONIBLE'),
(132, 'LIB-132', 'Economía del crimen', 'Becker', 2024, 'Economía', 'D4F', 'LIBRO', 4, 4, 'DISPONIBLE'),
(159, 'LIB-159', 'El amor en los tiempos del cólera', 'García Márquez', 2024, 'Literatura', 'C6F', 'LIBRO', 3, 3, 'DISPONIBLE'),
(158, 'LIB-158', 'El viejo y el mar', 'Hemingway', 2024, 'Literatura', 'B3A', 'LIBRO', 2, 2, 'DISPONIBLE'),
(151, 'LIB-151', 'Ensayo sobre el entendimiento humano', 'Locke', 2024, 'Filosofía', 'B5F', 'LIBRO', 3, 3, 'DISPONIBLE'),
(112, 'LIB-112', 'Física para todos', 'Isaac Newton', 2024, 'Fisica', 'D5A', 'LIBRO', 5, 5, 'DISPONIBLE'),
(130, 'LIB-130', 'Genética Humana', 'Mukherjee', 2024, 'Biología', 'B6B', 'LIBRO', 9, 9, 'DISPONIBLE'),
(111, 'LIB-111', 'Goleman', 'Psicología', 2024, 'Psicología', 'C1D', 'LIBRO', 1, 1, 'DISPONIBLE'),
(137, 'LIB-137', 'Graham', 'Finanzas', 2024, 'Finanzas', 'B3E', 'LIBRO', 3, 3, 'DISPONIBLE'),
(139, 'LIB-139', 'Harari', 'Historia', 2024, 'Historia', 'D6F', 'LIBRO', 4, 4, 'DISPONIBLE'),
(126, 'LIB-126', 'Hawking', 'Física', 2024, 'Física', 'C5B', 'LIBRO', 3, 3, 'DISPONIBLE'),
(107, 'LIB-107', 'Inteligencia Emocional', 'Goleman', 2024, 'Psicología', 'A5C', 'LIBRO', 8, 8, 'DISPONIBLE'),
(125, 'LIB-125', 'Java para Principiantes', 'Deitel', 2024, 'Informática', 'B1F', 'LIBRO', 2, 2, 'DISPONIBLE'),
(104, 'LIB-104', 'Kant', 'Filosofía', 2024, 'Filosofía', 'D4A', 'LIBRO', 1, 1, 'DISPONIBLE'),
(114, 'LIB-114', 'Kotler', 'Negocios', 2024, 'Negocios', 'B4A', 'LIBRO', 8, 8, 'DISPONIBLE'),
(106, 'LIB-106', 'Krugman', 'Economía', 2024, 'Economía', 'B1E', 'LIBRO', 2, 2, 'DISPONIBLE'),
(150, 'LIB-150', 'La República', 'Plato', 2024, 'Filosofía', 'A2D', 'LIBRO', 2, 2, 'DISPONIBLE'),
(153, 'LIB-153', 'La riqueza de las naciones', 'Smith', 2024, 'Economía', 'D1E', 'LIBRO', 6, 6, 'DISPONIBLE'),
(134, 'LIB-134', 'Mankiw', 'Economía', 2024, 'Economía', 'C6C', 'LIBRO', 4, 4, 'DISPONIBLE'),
(123, 'LIB-123', 'Marketing Digital', 'Kotler', 2024, 'Negocios', 'C6A', 'LIBRO', 7, 7, 'DISPONIBLE'),
(108, 'LIB-108', 'Martin', 'Informática', 2024, 'Informática', 'D2F', 'LIBRO', 6, 6, 'DISPONIBLE'),
(115, 'LIB-115', 'Motivación y Éxito', 'Pink', 2024, 'Psicología', 'A3D', 'LIBRO', 7, 7, 'DISPONIBLE'),
(109, 'LIB-109', 'Mukherjee', 'Biología', 2024, 'Biología', 'A5D', 'LIBRO', 2, 2, 'DISPONIBLE'),
(105, 'LIB-105', 'Norman', 'Diseño', 2024, 'Diseño', 'A6C', 'LIBRO', 5, 5, 'DISPONIBLE'),
(116, 'LIB-116', 'Origami Creativo', 'Smith', 2024, 'Arte', 'A3A', 'LIBRO', 3, 3, 'DISPONIBLE'),
(120, 'LIB-120', 'Pensar rápido,  pensar despacio', 'Kahneman', 2024, 'Psicología', 'A3A', 'LIBRO', 5, 5, 'DISPONIBLE'),
(135, 'LIB-135', 'Pink', 'Psicología', 2024, 'Psicología', 'D5B', 'LIBRO', 2, 2, 'DISPONIBLE'),
(154, 'LIB-154', 'Principios de economía política', 'Ricardo', 2024, 'Economía', 'A4C', 'LIBRO', 2, 2, 'DISPONIBLE'),
(117, 'LIB-117', 'Principios de Finanzas', 'Graham', 2024, 'Finanzas', 'D2F', 'LIBRO', 2, 2, 'DISPONIBLE'),
(128, 'LIB-128', 'Psychology', 'David G. Myers', 2024, 'Psicología', 'A3C', 'LIBRO', 2, 2, 'DISPONIBLE'),
(118, 'LIB-118', 'Química Orgánica', 'Brown', 2024, 'Química', 'C5B', 'LIBRO', 6, 6, 'DISPONIBLE'),
(119, 'LIB-119', 'Sapiens: Historia', 'Harari', 2024, 'Historia', 'A1A', 'LIBRO', 5, 5, 'DISPONIBLE'),
(136, 'LIB-136', 'Smith', 'Arte', 2024, 'Arte', 'A3A', 'LIBRO', 1, 1, 'DISPONIBLE'),
(152, 'LIB-152', 'Sobre la libertad', 'Mill', 2024, 'Filosofía', 'C5A', 'LIBRO', 4, 4, 'DISPONIBLE'),
(124, 'LIB-124', 'Start with Why', 'Simon Sinek', 2024, 'Liderazgo', 'C1D', 'LIBRO', 5, 5, 'DISPONIBLE'),
(103, 'LIB-103', 'Stewart', 'Matemáticas', 2024, 'Matemáticas', 'C3D', 'LIBRO', 4, 4, 'DISPONIBLE'),
(122, 'LIB-122', 'Stewart', 'Matemáticas', 2024, 'Matemáticas', 'C3D', 'LIBRO', 4, 4, 'DISPONIBLE'),
(155, 'LIB-155', 'Teoría general', 'Keynes', 2024, 'Economía', 'B6F', 'LIBRO', 2, 2, 'DISPONIBLE'),
(140, 'LIB-140', 'The Prince', 'Niccolò Machiavelli', 2024, 'Política', 'C4A', 'LIBRO', 5, 5, 'DISPONIBLE'),
(141, 'LIB-141', 'The Selfish Gene', 'Richard Dawkins', 2024, 'Biología', 'B4C', 'LIBRO', 3, 3, 'DISPONIBLE'),
(142, 'LIB-142', 'The Structure of Scientific Revolutions', 'Thomas Kuhn', 2024, 'Filosofía', 'D1A', 'LIBRO', 7, 7, 'DISPONIBLE'),
(143, 'LIB-143', 'Thinking,  Fast and Slow', 'Daniel Kahneman', 2024, 'Psicología', 'B5B', 'LIBRO', 2, 2, 'DISPONIBLE'),
(144, 'LIB-144', 'Tools of Titans', 'Tim Ferriss', 2024, 'Productividad', 'B5B', 'LIBRO', 4, 4, 'DISPONIBLE'),
(145, 'LIB-145', 'Understanding Media', 'Marshall McLuhan', 2024, 'Comunicación', 'C5B', 'LIBRO', 6, 6, 'DISPONIBLE'),
(102, 'LIB-102', 'Watson', 'Biología', 2024, 'Biología', 'B2F', 'LIBRO', 2, 2, 'DISPONIBLE'),
(121, 'LIB-121', 'Watson', 'Biología', 2024, 'Biología', 'B2F', 'LIBRO', 2, 2, 'DISPONIBLE'),
(146, 'LIB-146', 'What Is Life?', 'Erwin Schrödinger', 2024, 'Biología', 'B4C', 'LIBRO', 5, 5, 'DISPONIBLE'),
(147, 'LIB-147', 'Zen and the Art of Motorcycle Maintenance', 'Robert M. Pirsig', 2024, 'Filosofía', 'D1A', 'LIBRO', 3, 3, 'DISPONIBLE'),
(148, 'LIB-148', 'Zero to One', 'Peter Thiel', 2024, 'Negocios', 'C4A', 'LIBRO', 8, 8, 'DISPONIBLE');

INSERT INTO libros (id_documento, isbn, editorial) VALUES 
(149, 'S/I', 'S/I'),
(157, 'S/I', 'S/I'),
(133, 'S/I', 'S/I'),
(138, 'S/I', 'S/I'),
(127, 'S/I', 'S/I'),
(156, 'S/I', 'S/I'),
(113, 'S/I', 'S/I'),
(101, 'S/I', 'S/I'),
(160, 'S/I', 'S/I'),
(110, 'S/I', 'S/I'),
(131, 'S/I', 'S/I'),
(129, 'S/I', 'S/I'),
(132, 'S/I', 'S/I'),
(159, 'S/I', 'S/I'),
(158, 'S/I', 'S/I'),
(151, 'S/I', 'S/I'),
(112, 'S/I', 'S/I'),
(130, 'S/I', 'S/I'),
(111, 'S/I', 'S/I'),
(137, 'S/I', 'S/I'),
(139, 'S/I', 'S/I'),
(126, 'S/I', 'S/I'),
(107, 'S/I', 'S/I'),
(125, 'S/I', 'S/I'),
(104, 'S/I', 'S/I'),
(114, 'S/I', 'S/I'),
(106, 'S/I', 'S/I'),
(150, 'S/I', 'S/I'),
(153, 'S/I', 'S/I'),
(134, 'S/I', 'S/I'),
(123, 'S/I', 'S/I'),
(108, 'S/I', 'S/I'),
(115, 'S/I', 'S/I'),
(109, 'S/I', 'S/I'),
(105, 'S/I', 'S/I'),
(116, 'S/I', 'S/I'),
(120, 'S/I', 'S/I'),
(135, 'S/I', 'S/I'),
(154, 'S/I', 'S/I'),
(117, 'S/I', 'S/I'),
(128, 'S/I', 'S/I'),
(118, 'S/I', 'S/I'),
(119, 'S/I', 'S/I'),
(136, 'S/I', 'S/I'),
(152, 'S/I', 'S/I'),
(124, 'S/I', 'S/I'),
(103, 'S/I', 'S/I'),
(122, 'S/I', 'S/I'),
(155, 'S/I', 'S/I'),
(140, 'S/I', 'S/I'),
(141, 'S/I', 'S/I'),
(142, 'S/I', 'S/I'),
(143, 'S/I', 'S/I'),
(144, 'S/I', 'S/I'),
(145, 'S/I', 'S/I'),
(102, 'S/I', 'S/I'),
(121, 'S/I', 'S/I'),
(146, 'S/I', 'S/I'),
(147, 'S/I', 'S/I'),
(148, 'S/I', 'S/I');
