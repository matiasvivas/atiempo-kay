BEGIN; -- Iniciar una transacción

-- Insertar registros solo si no existen ya
INSERT INTO roles (role_id, role)
VALUES (987876, 'VENDER')
    ON CONFLICT (role_id) DO NOTHING;

INSERT INTO roles (role_id, role)
VALUES (796338, 'VENDERPRO')
    ON CONFLICT (role_id) DO NOTHING;

INSERT INTO roles (role_id, role)
VALUES (126338, 'ADMIN')
    ON CONFLICT (role_id) DO NOTHING;

COMMIT; -- Confirmar la transacción