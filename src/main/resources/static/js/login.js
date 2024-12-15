// login.js
document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector("form");
    const emailInput = document.querySelector("input[name='email']");
    const senhaInput = document.querySelector("input[name='senha']");
    const submitButton = document.querySelector("button[type='submit']");

    // Adiciona evento ao enviar o formulário
    form.addEventListener("submit", function (event) {
        if (!emailInput.value || !senhaInput.value) {
            event.preventDefault();
            alert("Preencha todos os campos.");
        } else {
            alert("Login efetuado com sucesso!");
        }
    });

    // Estética e animações
    form.querySelectorAll("input").forEach(input => {
        input.addEventListener("focus", function () {
            this.style.borderColor = "#3498db";
        });

        input.addEventListener("blur", function () {
            this.style.borderColor = "#ddd";
        });
    });

    submitButton.addEventListener("mouseover", function () {
        this.style.backgroundColor = "#2980b9";
    });

    submitButton.addEventListener("mouseout", function () {
        this.style.backgroundColor = "#3498db";
    });
});
