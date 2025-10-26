document.addEventListener('DOMContentLoaded', () => {

    // O nome 'gestao-coral-backend' vem do seu pom.xml
    // Esta é agora a nossa base para todas as chamadas de API
    const API_BASE_URL = '/gestao-coral-backend/api';

    // --- LÓGICA DE LOGIN (Ainda simulada) ---
    const loginForm = document.getElementById('login-form');
    if (loginForm) {
        loginForm.addEventListener('submit', (e) => {
            e.preventDefault();
            // TODO: A lógica de autenticação (UC01) ainda não foi criada no backend.
            // Por agora, vamos simular o sucesso.
            console.log("Login simulado com sucesso!");
            window.location.href = 'dashboard.html';
        });
    }

    // --- LÓGICA DE LOGOUT ---
    const logoutBtn = document.getElementById('logout-btn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', () => {
            window.location.href = 'index.html'; // Voltamos para o index.html (antigo login.html)
        });
    }

    // --- LÓGICA DA PÁGINA 'coristas.html' ---
    if (window.location.pathname.endsWith('coristas.html')) {
        const modal = document.getElementById('corista-modal');
        const addBtn = document.getElementById('add-corista-btn');
        const closeBtn = document.querySelector('.close-btn');
        const coristaForm = document.getElementById('corista-form');

        // Lógica do Modal
        addBtn.onclick = () => {
            document.getElementById('modal-title').innerText = 'Adicionar Novo Corista';
            coristaForm.reset();
            modal.style.display = 'flex';
        }
        closeBtn.onclick = () => modal.style.display = 'none';
        window.onclick = (event) => {
            if (event.target == modal) {
                modal.style.display = 'none';
            }
        }

        // Lógica do Formulário (CRIAR NOVO CORISTA - POST)
        coristaForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            
            // 1. Apanha os dados do formulário
            // O formulário HTML usa 'naipe', mas o nosso modelo Java usa 'tipoVoz'
            // Precisamos de fazer a "tradução" aqui.
            const coristaData = {
                nome: document.getElementById('nome').value,
                // O HTML tem 'email', mas o nosso modelo não. Vamos ignorá-lo por agora.
                // email: document.getElementById('email').value,
                tipoVoz: document.getElementById('naipe').value,
                ativo: true // Definimos 'ativo' como 'true' por defeito
            };

            try {
                // 2. Envia os dados (JSON) para o nosso CoristaServlet (doPost)
                const response = await fetch(`${API_BASE_URL}/coristas`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(coristaData)
                });

                if (!response.ok) {
                    throw new Error('Falha ao criar corista');
                }
                
                // 3. Se correu bem, fecha o modal e recarrega a lista
                modal.style.display = 'none';
                loadCoristas();

            } catch (error) {
                console.error("Erro ao salvar corista:", error);
                alert("Erro ao salvar. Verifique a consola.");
            }
        });

        // Carrega os dados quando a página abre
        loadCoristas();
    }

});

/**
 * Funções da API (agora são reais!)
 */

async function loadCoristas() {
    const tableBody = document.getElementById('coristas-table-body');
    if (!tableBody) return;

    // ----- ISTO FOI REMOVIDO -----
    // const mockData = [ ... ];
    // renderCoristas(mockData);
    // ----- FIM DA SIMULAÇÃO -----

    // CHAMADA REAL À API (CoristaServlet - doGet)
    try {
        const response = await fetch('/gestao-coral-backend/api/coristas'); // URL do nosso Servlet
        const coristas = await response.json(); // O GSON converteu para JSON
        renderCoristas(coristas);
    } catch (error) {
        console.error("Erro ao buscar coristas:", error);
        tableBody.innerHTML = '<tr><td colspan="4">Erro ao carregar dados. O back-end está a rodar?</td></tr>';
    }
}

function renderCoristas(coristas) {
    const tableBody = document.getElementById('coristas-table-body');
    tableBody.innerHTML = '';
    
    if (coristas.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="4">Nenhum corista encontrado.</td></tr>';
        return;
    }

    coristas.forEach(corista => {
        // Corrigimos os nomes dos campos:
        // O HTML pede "Nome", "Email", "Naipe/Instrumento"
        // O nosso Modelo Java tem "nome", "tipoVoz" (não temos email no modelo)
        
        const row = `
            <tr>
                <td>${corista.nome}</td>
                <td>(Não implementado no DB)</td>
                <td>${corista.tipoVoz || ''}</td>
                <td>
                    <button class="edit-btn" onclick="editCorista(${corista.id})">Editar</button>
                    <button class="delete-btn" onclick="deleteCorista(${corista.id})">Excluir</button>
                </td>
            </tr>
        `;
        tableBody.innerHTML += row;
    });
}

function editCorista(id) {
    alert(`Editar corista com ID: ${id}. A função de 'Editar' (Update/PUT) ainda não foi implementada no nosso Servlet.`);
    // TODO:
    // 1. Chamar `GET /api/coristas/{id}` (precisamos de implementar o 'doGet' por ID no CoristaServlet)
    // 2. Preencher o modal
    // 3. Mudar o 'submit' do formulário para fazer um 'PUT' (Update)
}

async function deleteCorista(id) {
    if (confirm(`Tem certeza que deseja excluir o corista com ID: ${id}?`)) {
        
        // CHAMADA REAL À API (CoristaServlet - doDelete)
        try {
            const response = await fetch(`/gestao-coral-backend/api/coristas/${id}`, {
                method: 'DELETE'
            });

            if (!response.ok) {
                throw new Error('Falha ao apagar corista');
            }
            
            // Recarrega a lista
            loadCoristas(); 

        } catch (error) {
            console.error("Erro ao apagar corista:", error);
            alert("Erro ao apagar. Verifique a consola.");
        }
    }
}