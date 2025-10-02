-- Dados iniciais para teste
INSERT INTO users (id, name, email, created_at) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'João Silva', 'joao.silva@email.com', CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440002', 'Maria Santos', 'maria.santos@email.com', CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440003', 'Pedro Oliveira', 'pedro.oliveira@email.com', CURRENT_TIMESTAMP);

INSERT INTO tasks (id, title, description, status, created_at, user_id) VALUES
('550e8400-e29b-41d4-a716-446655440011', 'Implementar API de Usuários', 'Criar endpoints para CRUD de usuários', 'IN_PROGRESS', CURRENT_TIMESTAMP, '550e8400-e29b-41d4-a716-446655440001'),
('550e8400-e29b-41d4-a716-446655440012', 'Configurar Banco de Dados', 'Configurar conexão e migrações do banco', 'COMPLETED', CURRENT_TIMESTAMP, '550e8400-e29b-41d4-a716-446655440001'),
('550e8400-e29b-41d4-a716-446655440013', 'Documentar API', 'Criar documentação Swagger da API', 'PENDING', CURRENT_TIMESTAMP, '550e8400-e29b-41d4-a716-446655440002');

INSERT INTO subtasks (id, title, description, status, created_at, task_id) VALUES
('550e8400-e29b-41d4-a716-446655440021', 'Criar endpoint POST /usuarios', 'Implementar criação de usuários', 'COMPLETED', CURRENT_TIMESTAMP, '550e8400-e29b-41d4-a716-446655440011'),
('550e8400-e29b-41d4-a716-446655440022', 'Criar endpoint GET /usuarios/{id}', 'Implementar busca de usuário por ID', 'IN_PROGRESS', CURRENT_TIMESTAMP, '550e8400-e29b-41d4-a716-446655440011'),
('550e8400-e29b-41d4-a716-446655440023', 'Adicionar validações', 'Implementar validações de entrada', 'PENDING', CURRENT_TIMESTAMP, '550e8400-e29b-41d4-a716-446655440011');
