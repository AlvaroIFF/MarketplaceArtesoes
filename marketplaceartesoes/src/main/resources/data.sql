-- Inserindo as Categorias
INSERT INTO categorias (id, nome) VALUES (1, 'Cerâmica');
INSERT INTO categorias (id, nome) VALUES (2, 'Têxteis');
INSERT INTO categorias (id, nome) VALUES (3, 'Jóias');
INSERT INTO categorias (id, nome) VALUES (4, 'Madeira');

-- Inserindo os Usuários
INSERT INTO usuarios (id, dtype, nome, cpf, dt_nasc, num_contato, email, senha, foto_url) VALUES (101, 'ARTESAO', 'Maria Silva', '11111111111', '1995-09-01', '21999999991', 'maria@email.com', 'senha123', null);
INSERT INTO usuarios (id, dtype, nome, cpf, dt_nasc, num_contato, email, senha, foto_url) VALUES (102, 'ARTESAO', 'João Costa', '22222222222', '1980-09-01', '11988888882', 'joao@email.com', 'senha123', null);
INSERT INTO usuarios (id, dtype, nome, cpf, dt_nasc, num_contato, email, senha, foto_url) VALUES (103, 'ARTESAO', 'Ana Pereira', '33333333333', '1997-09-01', '81977777773', 'ana@email.com', 'senha123', null);

-- Inserindo as Lojas e associando-as aos Artesãos
INSERT INTO lojas (id, nome, descricao, cnpj, artesao_id) VALUES (1, 'Ateliê da Maria', 'Cerâmicas artesanais', null, 101);
INSERT INTO lojas (id, nome, descricao, cnpj, artesao_id) VALUES (2, 'Fios e Nós', 'Bolsas e acessórios em crochê', null, 102);
INSERT INTO lojas (id, nome, descricao, cnpj, artesao_id) VALUES (3, 'Prata da Casa', 'Jóias artesanais em prata', null, 103);

-- Inserindo os Produtos e associando-os às Lojas
INSERT INTO produtos (id, nome, descricao, preco, estoque, imagem_principal_url, loja_id) VALUES (1, 'Vaso de Cerâmica', 'Vaso feito à mão', 120.00, 10, '/images/vaso-ceramica.jpg', 1);
INSERT INTO produtos (id, nome, descricao, preco, estoque, imagem_principal_url, loja_id) VALUES (2, 'Bolsa de Crochê', 'Bolsa de ombro colorida', 85.50, 15, '/images/bolsa-croche.jpg', 2);
INSERT INTO produtos (id, nome, descricao, preco, estoque, imagem_principal_url, loja_id) VALUES (3, 'Colar de Prata', 'Colar com pingente de lua', 250.00, 5, '/images/colar-prata.jpg', 3);
INSERT INTO produtos (id, nome, descricao, preco, estoque, imagem_principal_url, loja_id) VALUES (4, 'Conjunto de Banheiro', 'Guarda sabão, escova e cotonete', 45.00, 8, '/images/conjunto-banheiro.jpg', 1);
INSERT INTO produtos (id, nome, descricao, preco, estoque, imagem_principal_url, loja_id) VALUES (5, 'Toalhas de Algodão', 'Toalhas de banho e rosto', 60.00, 12, '/images/toalhas.jpg', 2);

-- Associando os Produtos às Categorias
INSERT INTO produto_categoria (produto_id, categoria_id) VALUES (1, 1); 
INSERT INTO produto_categoria (produto_id, categoria_id) VALUES (2, 2); 
INSERT INTO produto_categoria (produto_id, categoria_id) VALUES (3, 3);
INSERT INTO produto_categoria (produto_id, categoria_id) VALUES (4, 1);
INSERT INTO produto_categoria (produto_id, categoria_id) VALUES (5, 2);