BEGIN; -- Iniciar una transacción

INSERT INTO codigo (id, concepto, dias_restantes, fecha_activacion, monto, serial, tipo, user_id)
VALUES (1000, 'Codigo Configurativo', 9999, null, null, 'INICIAR', 4, null)
    ON CONFLICT (id) DO NOTHING;

-- Insertar registros solo si no existen ya
INSERT INTO codigo (id, concepto, dias_restantes, fecha_activacion, monto, serial, tipo, user_id)
VALUES (9999, 'Token WS Cumpleanos', 9999, null, null, 'aKJ24hs0dk4j2hE2PX', 7, null)
    ON CONFLICT (id) DO NOTHING;

INSERT INTO codigo (id, concepto, dias_restantes, fecha_activacion, monto, serial, tipo, user_id)
VALUES (9997, 'Token WS Productos Bajo Stock', 9999, null, null, 'aKJ24hs0dk4j2hE2PX', 7, null)
    ON CONFLICT (id) DO NOTHING;

INSERT INTO codigo (id, concepto, dias_restantes, fecha_activacion, monto, serial, tipo, user_id)
VALUES (9997, 'Token WS Productos Por Vencer', 9999, null, null, 'aKJ24hs0dk4j2hE2PX', 7, null)
    ON CONFLICT (id) DO NOTHING;

COMMIT; -- Confirmar la transacción