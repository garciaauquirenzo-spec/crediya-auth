CREATE TABLE IF NOT EXISTS usuarios (
  id                serial PRIMARY KEY,
  nombres           VARCHAR(100) NOT NULL,
  apellidos         VARCHAR(100) NOT NULL,
  fecha_nacimiento  DATE,
  direccion         VARCHAR(255),
  telefono          VARCHAR(30),
  correo_electronico VARCHAR(150) NOT NULL,
  salario_base      NUMERIC(15,2) NOT NULL,
  creado_en         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  actualizado_en    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT chk_salario_range CHECK (salario_base >= 0 AND salario_base <= 15000000)
);

-- Unicidad por email (case-insensitive)
CREATE UNIQUE INDEX IF NOT EXISTS ux_usuarios_email_ci
  ON usuarios (LOWER(correo_electronico));
