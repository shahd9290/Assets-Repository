INSERT INTO public.roles VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_USER'), (3, 'ROLE_VIEWER') ON CONFLICT (id) DO NOTHING;
INSERT INTO public.users VALUES (1, 'admin@tp01.com', '$2a$12$.HDfGfkwuWH8A4k4irqNM.kUooS9Cf.wfJewNmQTRgn1OR04x0axa', 'admin') ON CONFLICT (id) DO NOTHING;
INSERT INTO public.user_roles VALUES (1, 1) ON CONFLICT (user_id, role_id) DO NOTHING;