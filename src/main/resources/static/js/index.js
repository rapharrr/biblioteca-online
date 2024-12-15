// Lista de gêneros principais
// Lista de gêneros principais
const generosPrincipais = [
    "Fantasy", "Science Fiction", "Romance", "Mystery", "Thriller",
    "Adventure", "Biography", "History", "Children", "Horror",
    "Classic", "Fiction", "Drama", "Comedy"
];

// Função para listar os livros e dividi-los por gêneros
function listarLivros() {
    fetch('/api/livros')
        .then(response => response.json())
        .then(data => {
            livros = data;
            exibirLivrosPorCategorias();
        })
        .catch(error => {
            console.error('Erro ao listar livros:', error);
        });
}

// Exibir livros divididos por categorias
function exibirLivrosPorCategorias() {
    const container = document.getElementById('lista-livros-venda');
    container.innerHTML = '';

    const livrosPorCategoria = agruparPorCategoria(livros);

    for (const [categoria, livros] of Object.entries(livrosPorCategoria)) {
        const categoriaDiv = document.createElement('div');
        categoriaDiv.className = 'categoria';

        const tituloCategoria = document.createElement('h2');
        tituloCategoria.textContent = categoria;

        const livrosDiv = document.createElement('div');
        livrosDiv.className = 'livros';

        livros.forEach(livro => {
            const card = criarCardLivro(livro);
            livrosDiv.appendChild(card);
        });

        categoriaDiv.appendChild(tituloCategoria);
        categoriaDiv.appendChild(livrosDiv);
        container.appendChild(categoriaDiv);
    }
}

// Agrupar livros por categorias
function agruparPorCategoria(livros) {
    return livros.reduce((acc, livro) => {
        const categoriasLivro = livro.genero.split(",").filter(genero => generosPrincipais.includes(genero.trim()));
        categoriasLivro.forEach(categoria => {
            if (!acc[categoria]) acc[categoria] = [];
            acc[categoria].push(livro);
        });
        return acc;
    }, {});
}

function listarSugestoes() {
    const usuarioId = localStorage.getItem('usuarioId'); // Exemplo: ID armazenado no localStorage

    fetch(`/api/recomendacoes?usuarioId=${usuarioId}`)
        .then(response => response.json())
        .then(data => {
            const sugestoesDiv = document.getElementById('sugestoes-livros');
            const listaSugestoes = document.getElementById('lista-sugestoes');

            if (data.length > 0) {
                sugestoesDiv.style.display = 'block';
                listaSugestoes.innerHTML = '';

                data.forEach(livro => {
                    const card = document.createElement('div');
                    card.classList.add('card');
                    const capaUrl = livro.capaUrl || 'path/to/default-image.jpg';

                    card.innerHTML = `
              <a href="detalhesLivros.html?id=${livro.id}">
                <img src="${capaUrl}" alt="Capa de ${livro.titulo}" class="card-img">
              </a>
              <div class="card-body">
                <h3 class="card-title">${livro.titulo}</h3>
                <p class="card-author">Autor: ${livro.autor}</p>
                <p class="card-price">R$ ${livro.preco.toFixed(2)}</p>
              </div>
            `;
                    listaSugestoes.appendChild(card);
                });
            } else {
                sugestoesDiv.style.display = 'none';
            }
        })
        .catch(error => {
            console.error('Erro ao listar sugestões:', error);
        });
}





// Criar card para cada livro
function criarCardLivro(livro) {
    const card = document.createElement('div');
    card.className = 'card';
    const capaUrl = livro.capaUrl || 'path/to/default-image.jpg';

    card.innerHTML = `
      <a href="detalhesLivros.html?id=${livro.id}">
        <img src="${capaUrl}" alt="Capa de ${livro.titulo}" class="card-img">
      </a>
      <div class="card-body">
        <h3 class="card-title">${livro.titulo}</h3>
        <p class="card-author">Autor: ${livro.autor}</p>
        <p class="card-price">R$ ${livro.preco.toFixed(2)}</p>
        <p class="card-rating">Avaliação: ${livro.avaliacaoMedia ? livro.avaliacaoMedia.toFixed(1) : 'N/A'} ★</p>
      </div>
    `;
    return card;
}

// Chama listarSugestoes ao carregar a página, se o usuário estiver logado
document.addEventListener('DOMContentLoaded', () => {
    const usuarioLogado = document.querySelector('[th:if="${usuarioLogado}"]');
    if (usuarioLogado) {
        listarSugestoes();
    }
});

// Inicializar a exibição de livros
document.addEventListener('DOMContentLoaded', listarLivros);