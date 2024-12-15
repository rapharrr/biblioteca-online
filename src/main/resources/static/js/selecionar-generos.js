document.addEventListener("DOMContentLoaded", () => {
    const checkboxes = document.querySelectorAll('input[name="genero"]');
    const submitButton = document.getElementById("submitButton");

    checkboxes.forEach((checkbox) => {
        checkbox.addEventListener("change", () => {
            const selected = Array.from(checkboxes).filter(cb => cb.checked);
            submitButton.disabled = selected.length !== 3;

            // Desabilita outros checkboxes se já houver três selecionados
            if (selected.length >= 3) {
                checkboxes.forEach(cb => {
                    if (!cb.checked) cb.disabled = true;
                });
            } else {
                checkboxes.forEach(cb => cb.disabled = false);
            }
        });
    });

    const form = document.getElementById("generosForm");
    form.addEventListener("submit", async (event) => {
        event.preventDefault();
        const selectedGenres = Array.from(checkboxes)
            .filter(cb => cb.checked)
            .map(cb => cb.value);

        try {
            const response = await fetch('/api/salvar-generos', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ generos: selectedGenres }),
            });

            if (response.ok) {
                alert("Gêneros salvos com sucesso!");
                window.location.href = '/'; // Redirecione para a página inicial
            } else {
                alert("Erro ao salvar os gêneros. Tente novamente.");
            }
        } catch (error) {
            console.error("Erro:", error);
            alert("Erro ao salvar os gêneros. Verifique sua conexão.");
        }
    });
});
