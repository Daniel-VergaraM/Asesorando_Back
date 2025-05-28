delete from reserva_entity;
delete from comentario_entity;
delete from asesoria_entity;
delete from calendario_entity;
delete from usuario_entity_tematicas;
delete from tematica_entity;
delete from usuario_entity;

-- Crear estudiantes con URLs de retratos reales
INSERT INTO USUARIO_ENTITY (CODIGO_POSTAL, LATITUD, LONGITUD, ID, DTYPE, CONTRASENA, CORREO, ENLACE_REUNION, EXPERIENCIA, FORMACION, FOTO_URL, NOMBRE, PRECIO_HORA, TELEFONO, TIPO, VIDEO_URL)
VALUES
(110111, 4.60971, -74.08175, 1, 'EstudianteEntity', 'pass1234', 'maria.gomez@example.com', 'https://meet.google.com/maria', 'Participación en semillero de investigación', 'Ingeniería de Sistemas', 'https://randomuser.me/api/portraits/women/12.jpg', 'Maria Gomez', '0', '3001234567', 'ESTUDIANTE', 'https://youtube.com/maria/proyecto'),
(760001, 3.45164, -76.53198, 2, 'EstudianteEntity', 'securePass!', 'juan.perez@example.com', 'https://zoom.us/juan', 'Pasantía en multinacional', 'Administración de Empresas', 'https://randomuser.me/api/portraits/men/5.jpg', 'Juan Perez', '0', '3109876543', 'ESTUDIANTE', 'https://vimeo.com/juan/charla'),
(500001, 7.12345, -73.13496, 3, 'EstudianteEntity', 'qwerty789', 'laura.martinez@example.com', 'https://teams.microsoft.com/laura', 'Proyecto social universitario', 'Psicología', 'https://randomuser.me/api/portraits/women/15.jpg', 'Laura Martinez', '0', '3201122334', 'ESTUDIANTE', 'https://youtube.com/laura/psicologia'),
(050001, 6.2442, -75.5812, 4, 'EstudianteEntity', 'admin2024', 'carlos.lopez@example.com', 'https://zoom.us/carlos', 'Voluntariado en alfabetización digital', 'Comunicación Social', 'https://randomuser.me/api/portraits/men/7.jpg', 'Carlos Lopez', '0', '3015566778', 'ESTUDIANTE', 'https://vimeo.com/carlos/comunicacion'),
(190001, 2.44108, -76.6069, 5, 'EstudianteEntity', 'mypassword', 'paula.sanchez@example.com', 'https://meet.google.com/paula', 'Participante de Olimpiadas de Matemáticas', 'Matemáticas', 'https://randomuser.me/api/portraits/women/20.jpg', 'Paula Sanchez', '0', '3156789012', 'ESTUDIANTE', 'https://youtube.com/paula/olimpiadas'),
(150001, 5.53528, -73.36778, 6, 'EstudianteEntity', 'letmein123', 'luis.fernandez@example.com', 'https://meet.jit.si/luis', 'Monitor académico en Física', 'Física', 'https://randomuser.me/api/portraits/men/10.jpg', 'Luis Fernandez', '0', '3009988776', 'ESTUDIANTE', 'https://vimeo.com/luis/fisica'),
(200001, 8.76417, -75.87958, 7, 'EstudianteEntity', 'changeme', 'ana.garcia@example.com', 'https://zoom.us/ana', 'Investigadora junior en biología', 'Biología', 'https://randomuser.me/api/portraits/women/25.jpg', 'Ana Garcia', '0', '3023344556', 'ESTUDIANTE', 'https://youtube.com/ana/biologia'),
(730001, 4.43889, -75.23222, 8, 'EstudianteEntity', 'testuser1', 'david.moreno@example.com', 'https://meet.google.com/david', 'Auxiliar de laboratorio de química', 'Química', 'https://randomuser.me/api/portraits/men/12.jpg', 'David Moreno', '0', '3104455667', 'ESTUDIANTE', 'https://vimeo.com/david/quimica'),
(470001, 11.24079, -74.19904, 9, 'EstudianteEntity', 'demo123', 'andrea.castillo@example.com', 'https://teams.microsoft.com/andrea', 'Colaboración en revista estudiantil', 'Literatura', 'https://randomuser.me/api/portraits/women/18.jpg', 'Andrea Castillo', '0', '3206677889', 'ESTUDIANTE', 'https://youtube.com/andrea/literatura'),
(680001, 7.12539, -73.1198, 10, 'EstudianteEntity', 'springboot', 'julian.mendez@example.com', 'https://zoom.us/julian', 'Participante en hackathons de programación', 'Ingeniería de Software', 'https://randomuser.me/api/portraits/men/15.jpg', 'Julian Mendez', '0', '3005544332', 'ESTUDIANTE', 'https://vimeo.com/julian/codigo');

-- Crear profesores con URLs de retratos reales
INSERT INTO USUARIO_ENTITY (CODIGO_POSTAL, LATITUD, LONGITUD, ID, DTYPE, CONTRASENA, CORREO, ENLACE_REUNION, EXPERIENCIA, FORMACION, FOTO_URL, NOMBRE, PRECIO_HORA, TELEFONO, TIPO, VIDEO_URL)
VALUES
(110121, 4.6531, -74.0836, 101, 'ProfesorEntity', 'prof123', 'alberto.ruiz@example.com', 'https://meet.google.com/alberto', '10 años enseñando física en universidad', 'Física', 'https://randomuser.me/api/portraits/men/22.jpg', 'Alberto Ruiz', 80000, '3100001122', 'PROFESOR', 'https://youtube.com/alberto/fisica'),
(760045, 3.4372, -76.5222, 102, 'ProfesorEntity', 'pass456', 'beatriz.mendoza@example.com', 'https://zoom.us/beatriz', '5 años como tutora de matemáticas', 'Matemáticas', 'https://randomuser.me/api/portraits/women/28.jpg', 'Beatriz Mendoza', 60000, '3151234567', 'PROFESOR', 'https://vimeo.com/beatriz/matematicas'),
(500101, 7.1143, -73.1225, 103, 'ProfesorEntity', 'teach789', 'carlos.vera@example.com', 'https://teams.microsoft.com/carlos', 'Docente universitario con maestría en ingeniería', 'Ingeniería Electrónica', 'https://randomuser.me/api/portraits/men/25.jpg', 'Carlos Vera', 90000, '3113344556', 'PROFESOR', 'https://youtube.com/carlos/ingenieria'),
(130001, 10.3997, -75.5144, 104, 'ProfesorEntity', 'docente2025', 'diana.torres@example.com', 'https://meet.jit.si/diana', 'Profesora certificada en química y biotecnología', 'Química', 'https://randomuser.me/api/portraits/women/40.jpg', 'Diana Torres', 75000, '3167788990', 'PROFESOR', 'https://vimeo.com/diana/quimica'),
(170001, 5.0676, -75.5174, 105, 'ProfesorEntity', 'bio1234', 'eduardo.morales@example.com', 'https://zoom.us/eduardo', 'Investigador postdoctoral en biología', 'Biología', 'https://randomuser.me/api/portraits/men/30.jpg', 'Eduardo Morales', 95000, '3124455667', 'PROFESOR', 'https://youtube.com/eduardo/biologia'),
(110121, 4.6584, -74.0937, 11, 'ProfesorEntity', 'profpass1', 'marcela.ramirez@example.com', 'https://meet.google.com/marcela', '10 años como docente universitaria en matemáticas aplicadas', 'Doctorado en Matemáticas', 'https://randomuser.me/api/portraits/women/30.jpg', 'Marcela Ramirez', '60000', '3002223344', 'PROFESOR', 'https://youtube.com/marcela/matematicas'),
(760045, 3.4023, -76.5523, 12, 'ProfesorEntity', 'profpass2', 'jorge.torres@example.com', 'https://zoom.us/jorge', '7 años en docencia y tutorías personalizadas de física', 'Maestría en Física Teórica', 'https://randomuser.me/api/portraits/men/18.jpg', 'Jorge Torres', '55000', '3111234567', 'PROFESOR', 'https://vimeo.com/jorge/fisica'),
(500011, 7.1239, -73.1371, 13, 'ProfesorEntity', 'profpass3', 'natalia.diaz@example.com', 'https://teams.microsoft.com/natalia', 'Asesora de tesis y publicaciones en psicología clínica', 'Doctorado en Psicología Clínica', 'https://randomuser.me/api/portraits/women/32.jpg', 'Natalia Diaz', '70000', '3204455667', 'PROFESOR', 'https://youtube.com/natalia/psicologia'),
(050002, 6.2518, -75.5636, 14, 'ProfesorEntity', 'profpass4', 'ricardo.gomez@example.com', 'https://zoom.us/ricardo', 'Experto en comunicación y medios digitales, docente por 8 años', 'Maestría en Comunicación Estratégica', 'https://randomuser.me/api/portraits/men/20.jpg', 'Ricardo Gomez', '50000', '3029988776', 'PROFESOR', 'https://vimeo.com/ricardo/comunicacion'),
(190002, 2.4435, -76.6048, 15, 'ProfesorEntity', 'profpass5', 'silvia.morales@example.com', 'https://meet.google.com/silvia', 'Docente universitaria e investigadora en biotecnología vegetal', 'Doctorado en Biología Molecular', 'https://randomuser.me/api/portraits/women/35.jpg', 'Silvia Morales', '68000', '3157788990', 'PROFESOR', 'https://youtube.com/silvia/biotecnologia');


--Crear Tematicas  
INSERT INTO TEMATICA_ENTITY (ID, AREA, TEMA) VALUES (1, 'Matemáticas', 'Álgebra lineal');
INSERT INTO TEMATICA_ENTITY (ID, AREA, TEMA) VALUES (2, 'Matemáticas', 'Cálculo diferencial');
INSERT INTO TEMATICA_ENTITY (ID, AREA, TEMA) VALUES (3, 'Física', 'Mecánica clásica');
INSERT INTO TEMATICA_ENTITY (ID, AREA, TEMA) VALUES (4, 'Física', 'Termodinámica');
INSERT INTO TEMATICA_ENTITY (ID, AREA, TEMA) VALUES (5, 'Lenguaje', 'Comprensión lectora');
INSERT INTO TEMATICA_ENTITY (ID, AREA, TEMA) VALUES (6, 'Lenguaje', 'Gramática avanzada');
INSERT INTO TEMATICA_ENTITY (ID, AREA, TEMA) VALUES (7, 'Química', 'Reacciones químicas');
INSERT INTO TEMATICA_ENTITY (ID, AREA, TEMA) VALUES (8, 'Química', 'Estructura atómica');
INSERT INTO TEMATICA_ENTITY (ID, AREA, TEMA) VALUES (9, 'Biología', 'Genética');
INSERT INTO TEMATICA_ENTITY (ID, AREA, TEMA) VALUES (10, 'Biología', 'Evolución');


--Crear Usuario_Entity_Tematicas  
INSERT INTO USUARIO_ENTITY_TEMATICAS(PROFESORES_ID, TEMATICAS_ID) VALUES (101, 3); -- Alberto Ruiz → Mecánica clásica
INSERT INTO USUARIO_ENTITY_TEMATICAS(PROFESORES_ID, TEMATICAS_ID) VALUES (101, 4); -- Alberto Ruiz → Termodinámica
INSERT INTO USUARIO_ENTITY_TEMATICAS(PROFESORES_ID, TEMATICAS_ID) VALUES (102, 1); -- Beatriz Mendoza → Álgebra lineal
INSERT INTO USUARIO_ENTITY_TEMATICAS(PROFESORES_ID, TEMATICAS_ID) VALUES (102, 2); -- Beatriz Mendoza → Cálculo diferencial
INSERT INTO USUARIO_ENTITY_TEMATICAS(PROFESORES_ID, TEMATICAS_ID) VALUES (104, 7); -- Diana Torres → Reacciones químicas
INSERT INTO USUARIO_ENTITY_TEMATICAS(PROFESORES_ID, TEMATICAS_ID) VALUES (104, 8); -- Diana Torres → Estructura atómica
INSERT INTO USUARIO_ENTITY_TEMATICAS(PROFESORES_ID, TEMATICAS_ID) VALUES (105, 9); -- Eduardo Morales → Genética
INSERT INTO USUARIO_ENTITY_TEMATICAS(PROFESORES_ID, TEMATICAS_ID) VALUES (105, 10); -- Eduardo Morales → Evolución
INSERT INTO USUARIO_ENTITY_TEMATICAS(PROFESORES_ID, TEMATICAS_ID) VALUES (11, 1); -- Marcela Ramirez → Álgebra lineal
INSERT INTO USUARIO_ENTITY_TEMATICAS(PROFESORES_ID, TEMATICAS_ID) VALUES (11, 2); -- Marcela Ramirez → Cálculo diferencial


--Crear Calendario 
INSERT INTO CALENDARIO_ENTITY (FECHA_FIN, FECHA_INICIO, ID, PROFESOR_ID) VALUES ('2025-05-07 14:00:00', '2025-05-07 13:00:00', 1, 101);
INSERT INTO CALENDARIO_ENTITY (FECHA_FIN, FECHA_INICIO, ID, PROFESOR_ID) VALUES ('2025-05-08 11:30:00', '2025-05-08 10:00:00', 2, 102);
INSERT INTO CALENDARIO_ENTITY (FECHA_FIN, FECHA_INICIO, ID, PROFESOR_ID) VALUES ('2025-05-09 16:00:00', '2025-05-09 15:00:00', 3, 102);
INSERT INTO CALENDARIO_ENTITY (FECHA_FIN, FECHA_INICIO, ID, PROFESOR_ID) VALUES ('2025-05-10 12:00:00', '2025-05-10 11:15:00', 4, 101);
INSERT INTO CALENDARIO_ENTITY (FECHA_FIN, FECHA_INICIO, ID, PROFESOR_ID) VALUES ('2025-05-11 13:30:00', '2025-05-11 12:00:00', 5, 102);
INSERT INTO CALENDARIO_ENTITY (FECHA_FIN, FECHA_INICIO, ID, PROFESOR_ID) VALUES ('2025-05-12 14:00:00', '2025-05-12 13:00:00', 6, 101);
INSERT INTO CALENDARIO_ENTITY (FECHA_FIN, FECHA_INICIO, ID, PROFESOR_ID) VALUES ('2025-05-13 16:30:00', '2025-05-13 15:00:00', 7, 102);
INSERT INTO CALENDARIO_ENTITY (FECHA_FIN, FECHA_INICIO, ID, PROFESOR_ID) VALUES ('2025-05-14 12:00:00', '2025-05-14 11:00:00', 8, 103);
INSERT INTO CALENDARIO_ENTITY (FECHA_FIN, FECHA_INICIO, ID, PROFESOR_ID) VALUES ('2025-05-15 13:30:00', '2025-05-15 12:15:00', 9, 102);
INSERT INTO CALENDARIO_ENTITY (FECHA_FIN, FECHA_INICIO, ID, PROFESOR_ID) VALUES ('2025-05-16 14:30:00', '2025-05-16 13:00:00', 10, 103);


--Crear Comentario  
INSERT INTO COMENTARIO_ENTITY(CALIFICACION, ESTUDIANTE, ID, COMENTARIO) VALUES (5, 1, 1, 'La asesoría fue excelente, me ayudó mucho a entender la teoría de la mecánica cuántica.');
INSERT INTO COMENTARIO_ENTITY(CALIFICACION, ESTUDIANTE, ID, COMENTARIO) VALUES (4, 2, 2, 'La asesoría fue buena, pero creo que se podría haber explicado más detalladamente el cálculo diferencial.');
INSERT INTO COMENTARIO_ENTITY(CALIFICACION, ESTUDIANTE, ID, COMENTARIO) VALUES (5, 3, 3, 'Me encantó la asesoría, la profesora explicó muy bien los conceptos de psicología del desarrollo.');
INSERT INTO COMENTARIO_ENTITY(CALIFICACION, ESTUDIANTE, ID, COMENTARIO) VALUES (3, 4, 4, 'La asesoría fue útil, pero hubo algunos momentos en los que se hacía muy técnica la explicación de la teoría de la comunicación.');
INSERT INTO COMENTARIO_ENTITY(CALIFICACION, ESTUDIANTE, ID, COMENTARIO) VALUES (4, 5, 5, 'La asesoría estuvo bien, pero hubiera preferido más ejemplos prácticos de álgebra lineal.');
INSERT INTO COMENTARIO_ENTITY(CALIFICACION, ESTUDIANTE, ID, COMENTARIO) VALUES (5, 6, 6, 'Fue una excelente asesoría, entendí muy bien los temas de electromagnetismo. ¡Muy recomendable!');
INSERT INTO COMENTARIO_ENTITY(CALIFICACION, ESTUDIANTE, ID, COMENTARIO) VALUES (4, 7, 7, 'La clase fue bastante clara, aunque me gustaría que se profundizara un poco más en los detalles de la biología celular.');
INSERT INTO COMENTARIO_ENTITY(CALIFICACION, ESTUDIANTE, ID, COMENTARIO) VALUES (5, 8, 8, 'La asesoría fue increíble, el profesor tiene una gran forma de explicar la química orgánica. ¡Me ayudó mucho!');
INSERT INTO COMENTARIO_ENTITY(CALIFICACION, ESTUDIANTE, ID, COMENTARIO) VALUES (5, 9, 9, 'La asesoría fue excelente, la profesora explicó muy bien la literatura contemporánea y me dio muchos recursos adicionales.');
INSERT INTO COMENTARIO_ENTITY(CALIFICACION, ESTUDIANTE, ID, COMENTARIO) VALUES (4, 10, 10, 'La asesoría fue útil, aunque me gustaría que se hablara más sobre los detalles del desarrollo ágil de software.');
INSERT INTO ASESORIA_ENTITY (
  COMPLETADA, CALENDARIO_ID, ID, PROFESOR_ID, RESERVA_ID,
  USUARIO_ID, AREA, DURACION, TEMATICA, TIPO
) VALUES
  (FALSE, 1,  11, 101, NULL, 6, 'Física', '45', 'Ondas y sonido', 'Virtual'),
  (FALSE, 2,  12, 101, NULL, 7, 'Física', '60', 'Óptica física', 'Presencial'),
  (FALSE, 3,  13, 101, NULL, 8, 'Física', '30', 'Electromagnetismo avanzado', 'Virtual'),
  (FALSE, 4,  14, 101, NULL, 9, 'Física', '75', 'Termodinámica aplicada', 'Presencial'),
  (FALSE, 5,  15, 101, NULL,10, 'Física', '90', 'Mecánica de fluidos', 'Virtual'),

  (FALSE, 1,  11, 102, NULL, 6, 'Física', '45', 'Ondas y sonido', 'Virtual'),
  (FALSE, 2,  12, 102, NULL, 7, 'Física', '60', 'Óptica física', 'Presencial'),
  (FALSE, 3,  13, 102, NULL, 8, 'Física', '30', 'Electromagnetismo avanzado', 'Virtual'),
  (FALSE, 4,  14, 102, NULL, 9, 'Física', '75', 'Termodinámica aplicada', 'Presencial'),
  (FALSE, 5,  15, 102, NULL,10, 'Física', '90', 'Mecánica de fluidos', 'Virtual'),

  (FALSE, 1,  11, 105, NULL, 6, 'Física', '45', 'Ondas y sonido', 'Virtual'),
  (FALSE, 2,  12, 105, NULL, 7, 'Física', '60', 'Óptica física', 'Presencial'),
  (FALSE, 3,  13, 105, NULL, 8, 'Física', '30', 'Electromagnetismo avanzado', 'Virtual'),
  (FALSE, 4,  14, 105, NULL, 9, 'Física', '75', 'Termodinámica aplicada', 'Presencial'),
  (FALSE, 5,  15, 105, NULL,10, 'Física', '90', 'Mecánica de fluidos', 'Virtual')
  
  
  ;

  select * from USUARIO_ENTITY;


--Crear Asesorias 
INSERT INTO ASESORIA_ENTITY (COMPLETADA, CALENDARIO_ID, ID, PROFESOR_ID, RESERVA_ID, USUARIO_ID, AREA, DURACION, TEMATICA, TIPO) VALUES (FALSE, 1, 1, 101, NULL, 1, 'Física', 60, 'Mecánica cuántica', 'Virtual');
INSERT INTO ASESORIA_ENTITY (COMPLETADA, CALENDARIO_ID, ID, PROFESOR_ID, RESERVA_ID, USUARIO_ID, AREA, DURACION, TEMATICA, TIPO) VALUES (FALSE, 2, 2, 102, NULL, 2, 'Matemáticas', 90, 'Cálculo diferencial', 'Presencial');
INSERT INTO ASESORIA_ENTITY (COMPLETADA, CALENDARIO_ID, ID, PROFESOR_ID, RESERVA_ID, USUARIO_ID, AREA, DURACION, TEMATICA, TIPO) VALUES (FALSE, 3, 3, 102, NULL, 3, 'Psicología', 60, 'Psicología del desarrollo', 'Virtual');
INSERT INTO ASESORIA_ENTITY (COMPLETADA, CALENDARIO_ID, ID, PROFESOR_ID, RESERVA_ID, USUARIO_ID, AREA, DURACION, TEMATICA, TIPO) VALUES (FALSE, 4, 4, 101, NULL, 4, 'Comunicación Social', 75, 'Teoría de la comunicación', 'Presencial');
INSERT INTO ASESORIA_ENTITY (COMPLETADA, CALENDARIO_ID, ID, PROFESOR_ID, RESERVA_ID, USUARIO_ID, AREA, DURACION, TEMATICA, TIPO) VALUES (FALSE, 5, 5, 102, NULL, 5, 'Matemáticas', 90, 'Álgebra lineal', 'Virtual');
INSERT INTO ASESORIA_ENTITY (COMPLETADA, CALENDARIO_ID, ID, PROFESOR_ID, RESERVA_ID, USUARIO_ID, AREA, DURACION, TEMATICA, TIPO) VALUES (FALSE, 6, 6, 101, NULL, 6, 'Física', 60, 'Electromagnetismo', 'Presencial');
INSERT INTO ASESORIA_ENTITY (COMPLETADA, CALENDARIO_ID, ID, PROFESOR_ID, RESERVA_ID, USUARIO_ID, AREA, DURACION, TEMATICA, TIPO) VALUES (FALSE, 7, 7, 102, NULL, 7, 'Biología', 90, 'Biología celular', 'Virtual');
INSERT INTO ASESORIA_ENTITY (COMPLETADA, CALENDARIO_ID, ID, PROFESOR_ID, RESERVA_ID, USUARIO_ID, AREA, DURACION, TEMATICA, TIPO) VALUES (FALSE, 8, 8, 103, NULL, 8, 'Química', 60, 'Química orgánica', 'Presencial');
INSERT INTO ASESORIA_ENTITY (COMPLETADA, CALENDARIO_ID, ID, PROFESOR_ID, RESERVA_ID, USUARIO_ID, AREA, DURACION, TEMATICA, TIPO) VALUES (FALSE, 9, 9, 102, NULL, 9, 'Literatura', 75, 'Literatura contemporánea', 'Virtual');
INSERT INTO ASESORIA_ENTITY (COMPLETADA, CALENDARIO_ID, ID, PROFESOR_ID, RESERVA_ID, USUARIO_ID, AREA, DURACION, TEMATICA, TIPO) VALUES (FALSE, 10, 10, 103, NULL, 10, 'Ingeniería de Software', 90, 'Desarrollo ágil de software', 'Presencial');

INSERT INTO ASESORIA_ENTITY (COMPLETADA, CALENDARIO_ID, ID, PROFESOR_ID, RESERVA_ID, USUARIO_ID, AREA, DURACION, TEMATICA, TIPO) VALUES (FALSE, 1, 11, 101, NULL, 6, 'Física', '45', 'Ondas y sonido', 'Virtual');
INSERT INTO ASESORIA_ENTITY (COMPLETADA, CALENDARIO_ID, ID, PROFESOR_ID, RESERVA_ID, USUARIO_ID, AREA, DURACION, TEMATICA, TIPO) VALUES (FALSE, 2, 12, 101, NULL, 7, 'Física', '60', 'Óptica física', 'Presencial');
INSERT INTO ASESORIA_ENTITY (COMPLETADA, CALENDARIO_ID, ID, PROFESOR_ID, RESERVA_ID, USUARIO_ID, AREA, DURACION, TEMATICA, TIPO) VALUES (FALSE, 3, 13, 101, NULL, 8, 'Física', '30', 'Electromagnetismo avanzado', 'Virtual');
INSERT INTO ASESORIA_ENTITY (COMPLETADA, CALENDARIO_ID, ID, PROFESOR_ID, RESERVA_ID, USUARIO_ID, AREA, DURACION, TEMATICA, TIPO) VALUES (FALSE, 4, 14, 101, NULL, 9, 'Física', '75', 'Termodinámica aplicada', 'Presencial');
INSERT INTO ASESORIA_ENTITY (COMPLETADA, CALENDARIO_ID, ID, PROFESOR_ID, RESERVA_ID, USUARIO_ID, AREA, DURACION, TEMATICA, TIPO) VALUES (FALSE, 5, 15, 101, NULL, 10, 'Física', '90', 'Mecánica de fluidos', 'Virtual');


--Crear Reserva_Entity  
INSERT INTO RESERVA_ENTITY(CANCELADA, FECHA_RESERVA, ASESORIA, CALENDARIO, COMENTARIO, ESTUDIANTE, ID, ESTADO)VALUES (FALSE, '2025-05-04', 1, 1, 1, 1, 1001, 'Completada');
INSERT INTO RESERVA_ENTITY(CANCELADA, FECHA_RESERVA, ASESORIA, CALENDARIO, COMENTARIO, ESTUDIANTE, ID, ESTADO)VALUES (FALSE, '2025-05-04', 2, 2, 2, 2, 1002, 'Completada');
INSERT INTO RESERVA_ENTITY(CANCELADA, FECHA_RESERVA, ASESORIA, CALENDARIO, COMENTARIO, ESTUDIANTE, ID, ESTADO)VALUES (FALSE, '2025-05-04', 3, 3, 3, 3, 1003, 'Completada');
INSERT INTO RESERVA_ENTITY(CANCELADA, FECHA_RESERVA, ASESORIA, CALENDARIO, COMENTARIO, ESTUDIANTE, ID, ESTADO)VALUES (FALSE, '2025-05-04', 4, 4, 4, 4, 1004, 'Completada');
INSERT INTO RESERVA_ENTITY(CANCELADA, FECHA_RESERVA, ASESORIA, CALENDARIO, COMENTARIO, ESTUDIANTE, ID, ESTADO)VALUES (FALSE, '2025-05-04', 5, 5, 5, 5, 1005, 'Completada');
INSERT INTO RESERVA_ENTITY(CANCELADA, FECHA_RESERVA, ASESORIA, CALENDARIO, COMENTARIO, ESTUDIANTE, ID, ESTADO)VALUES (FALSE, '2025-05-04', 6, 6, 6, 6, 1006, 'Completada');
INSERT INTO RESERVA_ENTITY(CANCELADA, FECHA_RESERVA, ASESORIA, CALENDARIO, COMENTARIO, ESTUDIANTE, ID, ESTADO)VALUES (FALSE, '2025-05-04', 7, 7, 7, 7, 1007, 'Completada');
INSERT INTO RESERVA_ENTITY(CANCELADA, FECHA_RESERVA, ASESORIA, CALENDARIO, COMENTARIO, ESTUDIANTE, ID, ESTADO)VALUES (FALSE, '2025-05-04', 8, 8, 8, 8, 1008, 'Completada');
INSERT INTO RESERVA_ENTITY(CANCELADA, FECHA_RESERVA, ASESORIA, CALENDARIO, COMENTARIO, ESTUDIANTE, ID, ESTADO)VALUES (FALSE, '2025-05-04', 9, 9, 9, 9, 1009, 'Completada');
INSERT INTO RESERVA_ENTITY(CANCELADA, FECHA_RESERVA, ASESORIA, CALENDARIO, COMENTARIO, ESTUDIANTE, ID, ESTADO)VALUES (FALSE, '2025-05-04', 10, 10, 10, 10, 1010, 'Completada');

SELECT * FROM ASESORIA_ENTITY ;
SELECT * FROM CALENDARIO_ENTITY  ;
SELECT * FROM COMENTARIO_ENTITY ;
SELECT * FROM RESERVA_ENTITY  ;
SELECT * FROM TEMATICA_ENTITY  ;
SELECT * FROM USUARIO_ENTITY ;
SELECT * FROM USUARIO_ENTITY_TEMATICAS  ;