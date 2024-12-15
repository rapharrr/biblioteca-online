// registro.js
document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector("form");
    const nomeInput = document.querySelector("input[name='nome']");
    const emailInput = document.querySelector("input[name='email']");
    const senhaInput = document.querySelector("input[name='senha']");
    const submitButton = document.querySelector("button[type='submit']");

    // Adiciona evento ao enviar o formulário
    form.addEventListener("submit", function (event) {
        if (!nomeInput.value || !emailInput.value || !senhaInput.value) {
            event.preventDefault();
            alert("Todos os campos são obrigatórios.");
        } else {
            // Aqui pode adicionar validação adicional se necessário
            alert("Registro realizado com sucesso!");
        }
    });

    form.querySelectorAll("input").forEach(input => {
        input.addEventListener("focus", function () {
            this.style.borderColor = "#2ecc71";
        });

        input.addEventListener("blur", function () {
            this.style.borderColor = "#ddd";
        });
    });

    submitButton.addEventListener("mouseover", function () {
        this.style.backgroundColor = "#27ae60";
    });

    submitButton.addEventListener("mouseout", function () {
        this.style.backgroundColor = "#2ecc71";
    });
});
