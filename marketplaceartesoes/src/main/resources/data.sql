-- =========================
-- Inserindo Categorias
-- =========================
INSERT INTO categorias (nome) VALUES ('Cerâmica');
INSERT INTO categorias (nome) VALUES ('Têxteis');
INSERT INTO categorias (nome) VALUES ('Jóias');
INSERT INTO categorias (nome) VALUES ('Madeira');

-- =========================
-- Inserindo Usuários
-- =========================
INSERT INTO usuarios (tipo_usuario, nome, cpf, dt_nasc, num_contato, email, senha, foto_url) 
VALUES ('ARTESAO', 'Maria Silva', '11111111111', '1995-09-01', '21999999991', 'maria@email.com', 'senha123', '/images/maria-silva.png');

INSERT INTO usuarios (tipo_usuario, nome, cpf, dt_nasc, num_contato, email, senha, foto_url) 
VALUES ('ARTESAO', 'João Costa', '22222222222', '1980-09-01', '11988888882', 'joao@email.com', 'senha123', '/images/joao-costa.png');

INSERT INTO usuarios (tipo_usuario, nome, cpf, dt_nasc, num_contato, email, senha, foto_url) 
VALUES ('ARTESAO', 'Ana Pereira', '33333333333', '1997-09-01', '81977777773', 'ana@email.com', 'senha123', '/images/ana-pereira.png');

INSERT INTO usuarios (tipo_usuario, nome, cpf, dt_nasc, num_contato, email, senha, foto_url) 
VALUES ('ARTESAO', 'José Oliveira', '44444444444', '1970-06-13', '22999871777', 'jose@email.com', 'senha123', '/images/jose-oliveira.png');

INSERT INTO usuarios (tipo_usuario, nome, cpf, dt_nasc, num_contato, email, senha, foto_url) 
VALUES ('CLIENTE', 'Carlos Souza', '55555555555', '1990-05-15', '31999999994', 'carlos@email.com', 'senha123', '/images/carlos-souza.png');

-- =========================
-- Inserindo Lojas
-- =========================
INSERT INTO lojas (nome, descricao, cnpj, artesao_id, imagem_banner_url, dt_criacao) 
VALUES ('Ateliê da Maria', 'Cerâmicas artesanais', null, 1, '/images/maria-banner.png', CURRENT_DATE);

INSERT INTO lojas (nome, descricao, cnpj, artesao_id, imagem_banner_url, dt_criacao) 
VALUES ('Fios e Nós', 'Bolsas e acessórios em crochê', null, 2, '/images/joao-banner.png', CURRENT_DATE);

INSERT INTO lojas (nome, descricao, cnpj, artesao_id, imagem_banner_url, dt_criacao) 
VALUES ('Prata da Casa', 'Jóias artesanais em prata', null, 3, '/images/ana-banner.png', CURRENT_DATE);

INSERT INTO lojas (nome, descricao, cnpj, artesao_id, imagem_banner_url, dt_criacao) 
VALUES ('Ateliê do José', 'Móveis e decoração em madeira', null, 4, '/images/jose-banner.png', CURRENT_DATE);

-- =========================
-- Inserindo Produtos
-- =========================
INSERT INTO produtos (nome, descricao, preco, estoque, imagem_principal_url, loja_id, dt_criacao)
VALUES ('Vaso de Cerâmica', 'Vaso feito à mão', 120.00, 10, '/images/vaso-ceramica.jpg', 1, CURRENT_DATE);

INSERT INTO produtos (nome, descricao, preco, estoque, imagem_principal_url, loja_id, dt_criacao)
VALUES ('Bolsa de Crochê', 'Bolsa de ombro colorida', 85.50, 15, '/images/bolsa-croche.jpg', 2, CURRENT_DATE);

INSERT INTO produtos (nome, descricao, preco, estoque, imagem_principal_url, loja_id, dt_criacao)
VALUES ('Colar de Prata', 'Colar com pingente de lua', 250.00, 5, '/images/colar-prata.jpg', 3, CURRENT_DATE);

INSERT INTO produtos (nome, descricao, preco, estoque, imagem_principal_url, loja_id, dt_criacao)
VALUES ('Utensílios de Banheiro', 'Guarda sabão, escova e cotonete', 45.00, 8, '/images/conjunto-banheiro.jpg', 1, CURRENT_DATE);

INSERT INTO produtos (nome, descricao, preco, estoque, imagem_principal_url, loja_id, dt_criacao)
VALUES ('Toalhas de Algodão', 'Toalhas de banho e rosto', 60.00, 12, '/images/toalhas.jpg', 2, CURRENT_DATE);

INSERT INTO produtos (nome, descricao, preco, estoque, imagem_principal_url, loja_id, dt_criacao)
VALUES ('Conjunto para Cama', 'Conjunto para roupa de cama completo', 220.00, 5, '/images/conjunto-roupas-cama.png', 2, CURRENT_DATE);

INSERT INTO produtos (nome, descricao, preco, estoque, imagem_principal_url, loja_id, dt_criacao)
VALUES ('Boneca de Pano', 'Boneca de pano feita à mão', 80.00, 10, '/images/boneca.png', 2, CURRENT_DATE);

INSERT INTO produtos (nome, descricao, preco, estoque, imagem_principal_url, loja_id, dt_criacao)
VALUES ('Conjunto de Banheiro de Crochê', 'Conjunto de banheiro com tapete', 100.00, 10, '/images/conjunto-banheiro.png', 2, CURRENT_DATE);

INSERT INTO produtos (nome, descricao, preco, estoque, imagem_principal_url, loja_id, dt_criacao)
VALUES ('Mesa de Madeira', 'Mesa de madeira feita à mão', 300.00, 5, '/images/mesa.png', 4, CURRENT_DATE);

INSERT INTO produtos (nome, descricao, preco, estoque, imagem_principal_url, loja_id, dt_criacao)
VALUES ('Baú de Madeira', 'Baú de madeira feito à mão', 200.00, 5, '/images/bau.png', 4, CURRENT_DATE);

INSERT INTO produtos (nome, descricao, preco, estoque, imagem_principal_url, loja_id, dt_criacao)
VALUES ('Utensílios de Madeira', 'Utensílios de madeira feitos à mão', 150.00, 5, '/images/utensilios.png', 4, CURRENT_DATE);

INSERT INTO produtos (nome, descricao, preco, estoque, imagem_principal_url, loja_id, dt_criacao)
VALUES ('Cesta de Madeira', 'Cesta de frutas feita à mão', 80.00, 5, '/images/cesta.png', 4, CURRENT_DATE);

INSERT INTO produtos (nome, descricao, preco, estoque, imagem_principal_url, loja_id, dt_criacao)
VALUES ('Pulseira de Prata', 'Pulseira de prata feita à mão', 150.00, 5, '/images/pulseira.png', 3, CURRENT_DATE);

INSERT INTO produtos (nome, descricao, preco, estoque, imagem_principal_url, loja_id, dt_criacao)
VALUES ('Brinco de Ouro', 'Brinco de ouro feito à mão', 400.00, 5, '/images/brinco.png', 3, CURRENT_DATE);

INSERT INTO produtos (nome, descricao, preco, estoque, imagem_principal_url, loja_id, dt_criacao)
VALUES ('Corrente de Prata', 'Corrente de prata feita à mão', 200.00, 5, '/images/corrente.png', 3, CURRENT_DATE);

INSERT INTO produtos (nome, descricao, preco, estoque, imagem_principal_url, loja_id, dt_criacao)
VALUES ('Anel de Ouro', 'Anel de ouro feito à mão', 500.00, 5, '/images/anel.png', 3, CURRENT_DATE);

INSERT INTO produtos (nome, descricao, preco, estoque, imagem_principal_url, loja_id, dt_criacao)
VALUES ('Travessa de Cerâmica', 'Travessa de cerâmica azul feita à mão', 200.00, 5, '/images/travessa.png', 1, CURRENT_DATE);

INSERT INTO produtos (nome, descricao, preco, estoque, imagem_principal_url, loja_id, dt_criacao)
VALUES ('Conjunto de Pratos', 'Conjunto de pratos de cerâmica feitos à mão', 300.00, 5, '/images/conjunto-de-pratos.png', 1, CURRENT_DATE);

INSERT INTO produtos (nome, descricao, preco, estoque, imagem_principal_url, loja_id, dt_criacao)
VALUES ('Tigela de Cerâmica', 'Tigela de cerâmica feita à mão', 150.00, 5, '/images/tigela.png', 1, CURRENT_DATE);

INSERT INTO produtos (nome, descricao, preco, estoque, imagem_principal_url, loja_id, dt_criacao)
VALUES ('Escultura de Dragão', 'Escultura de dragão de madeira feita à mão', 200.00, 5, '/images/dragao-madeira.jpg', 4, CURRENT_DATE);

-- =========================
-- Associando Produtos às Categorias
-- =========================
INSERT INTO produto_categoria (produto_id, categoria_id) VALUES (1, 1);
INSERT INTO produto_categoria (produto_id, categoria_id) VALUES (2, 2);
INSERT INTO produto_categoria (produto_id, categoria_id) VALUES (3, 3);
INSERT INTO produto_categoria (produto_id, categoria_id) VALUES (4, 1);
INSERT INTO produto_categoria (produto_id, categoria_id) VALUES (5, 2);
INSERT INTO produto_categoria (produto_id, categoria_id) VALUES (6, 2);
INSERT INTO produto_categoria (produto_id, categoria_id) VALUES (7, 2);
INSERT INTO produto_categoria (produto_id, categoria_id) VALUES (8, 2);
INSERT INTO produto_categoria (produto_id, categoria_id) VALUES (9, 4);
INSERT INTO produto_categoria (produto_id, categoria_id) VALUES (10, 4);
INSERT INTO produto_categoria (produto_id, categoria_id) VALUES (11, 4);
INSERT INTO produto_categoria (produto_id, categoria_id) VALUES (12, 4);
INSERT INTO produto_categoria (produto_id, categoria_id) VALUES (13, 3);
INSERT INTO produto_categoria (produto_id, categoria_id) VALUES (14, 3);
INSERT INTO produto_categoria (produto_id, categoria_id) VALUES (15, 3);
INSERT INTO produto_categoria (produto_id, categoria_id) VALUES (16, 3);
INSERT INTO produto_categoria (produto_id, categoria_id) VALUES (17, 1);
INSERT INTO produto_categoria (produto_id, categoria_id) VALUES (18, 1);
INSERT INTO produto_categoria (produto_id, categoria_id) VALUES (19, 1);
INSERT INTO produto_categoria (produto_id, categoria_id) VALUES (20, 4);
