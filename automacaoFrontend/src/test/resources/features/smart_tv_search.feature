# language: pt
@smart_tv
Funcionalidade: Busca de Smart TVs na Americanas
  Como um usuário do site Americanas
  Eu quero buscar Smart TVs com filtro de preço
  Para encontrar produtos acima de R$3500

  Cenário: Buscar Smart TVs e filtrar por preço
    Dado que acesso o site da Americanas
    Quando digito "Smart TV" no campo de busca
    E filtro o preço por R$ 2.500 - R$ 5.000
    Então imprimo as informações dos produtos com valor maior que R$3500