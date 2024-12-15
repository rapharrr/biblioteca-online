let livros = []; // Array para armazenar os livros carregados

  // Função para carregar os livros da API e exibir na página
  function carregarLivros() {
    fetch('/api/livros')
      .then(response => response.json())
      .then(data => {
        livros = data; // Armazena os livros carregados
        exibirLivros(livros); // Exibe os livros na página
      })
      .catch(error => {
        console.error('Erro ao carregar os livros:', error);
      });
  }

  // Função para exibir os livros na página
  function exibirLivros(livros) {
    const livrosContainer = document.getElementById('livros-container');
    livrosContainer.innerHTML = ''; // Limpa o conteúdo atual

    livros.forEach(livro => {
      const card = document.createElement('div');
      card.classList.add('livro-card');
      card.innerHTML = `
        <img src="${livro.capaUrl || 'caminho/imagem-padrao.jpg'}" alt="Capa de ${livro.titulo}">
        <h3>${livro.titulo}</h3>
        <p>Autor: ${livro.autor}</p>
        <p>Ano de Publicação: ${livro.anoPublicacao}</p>
      `;
      livrosContainer.appendChild(card);
    });
  }

  // Função para filtrar os livros com base no texto da pesquisa
  function filtrarLivros() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();

    const livrosFiltrados = livros.filter(livro => {
      return livro.titulo.toLowerCase().includes(searchTerm) || livro.autor.toLowerCase().includes(searchTerm);
    });

    exibirLivros(livrosFiltrados); // Exibe os livros filtrados
  }

  // Carrega os livros quando a página for carregada
  document.addEventListener('DOMContentLoaded', carregarLivros);

