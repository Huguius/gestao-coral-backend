// Definida globalmente para que todas as funções a possam usar
const API_BASE_URL = '/gestao-coral-backend/api';

document.addEventListener('DOMContentLoaded', () => {
    // A linha que estava aqui foi movida para cima

    // --- LÓGICA DE LOGIN (Agora real!) ---
    const loginForm = document.getElementById('login-form');
    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => { // Adicionado 'async'
            e.preventDefault();

            const usernameInput = document.getElementById('username');
            const passwordInput = document.getElementById('password');

            const loginData = {
                username: usernameInput.value,
                password: passwordInput.value
            };

            try {
                // Chama o nosso LoginServlet via POST
                const response = await fetch(`${API_BASE_URL}/auth/login`, { // URL do LoginServlet
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(loginData)
                });

                const result = await response.json(); // Lê a resposta JSON do servlet

                if (response.ok && result.success) {
                    // Sucesso! Redireciona para o dashboard
                    console.log("Login real com sucesso para:", result.username);
                    window.location.href = 'dashboard.html';
                } else {
                    // Falha no login, mostra mensagem de erro
                    alert(result.message || 'Falha no login.');
                    passwordInput.value = ''; // Limpa a senha
                }

            } catch (error) {
                console.error("Erro durante o login:", error);
                alert("Erro ao tentar fazer login. Verifique a consola.");
            }
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
            document.getElementById('corista-id').value = ''; // Garante que o ID está limpo ao adicionar
            modal.style.display = 'flex';
        }
        closeBtn.onclick = () => modal.style.display = 'none';
        window.onclick = (event) => {
            if (event.target == modal) {
                modal.style.display = 'none';
            }
        }


    // Lógica do Formulário (CRIAR NOVO - POST ou EDITAR EXISTENTE - PUT)
        coristaForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            const coristaIdInput = document.getElementById('corista-id');
            const coristaId = coristaIdInput.value; // Pega o ID (pode estar vazio se for 'novo')

            // 1. Apanha os dados do formulário
            const coristaData = {
                nome: document.getElementById('nome').value,
                tipoVoz: document.getElementById('naipe').value,
                ativo: true // Assume ativo = true para simplificar
                // Se quiséssemos editar 'ativo', precisaríamos de um checkbox no HTML
            };

            // Determina o método (POST ou PUT) e o URL
            const isEditing = coristaId !== '';
            const method = isEditing ? 'PUT' : 'POST';
            const url = isEditing ? `${API_BASE_URL}/coristas/${coristaId}` : `${API_BASE_URL}/coristas`;

            console.log(`Salvando corista: ID=${coristaId || 'Novo'}, Método=${method}, URL=${url}`);

            try {
                // 2. Envia os dados (JSON) para o CoristaServlet (doPost ou doPut)
                const response = await fetch(url, {
                    method: method,
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(coristaData)
                });

                if (!response.ok) {
                    const errorText = await response.text(); // Tenta ler a mensagem de erro do servidor
                    throw new Error(`Falha ao salvar corista (Status: ${response.status}): ${errorText}`);
                }

                // 3. Se correu bem, fecha o modal e recarrega a lista
                modal.style.display = 'none';
                coristaIdInput.value = ''; // Limpa o ID escondido para a próxima vez
                loadCoristas();

            } catch (error) {
                console.error("Erro ao salvar corista:", error);
                alert(`Erro ao salvar. Verifique a consola.\nDetalhe: ${error.message}`);
            }
        });

        // Carrega os dados quando a página abre
        loadCoristas();
    }

    // --- NOVA LÓGICA PARA A PÁGINA 'musicos.html' ---
    if (window.location.pathname.endsWith('musicos.html')) {
        console.log("Página musicos.html carregada."); // Para depuração
        const modal = document.getElementById('musico-modal');
        const addBtn = document.getElementById('add-musico-btn');
        const closeBtn = modal.querySelector('.close-btn'); // Busca o botão dentro do modal
        const musicoForm = document.getElementById('musico-form');

        // Lógica do Modal (igual à dos coristas, mas com IDs diferentes)
        addBtn.onclick = () => {
            document.getElementById('modal-title').innerText = 'Adicionar Novo Músico';
            musicoForm.reset();
            document.getElementById('musico-id').value = ''; // Limpa o ID escondido
            document.getElementById('musico-ativo').checked = true; // Garante que 'ativo' começa marcado
            modal.style.display = 'flex';
        }
        closeBtn.onclick = () => modal.style.display = 'none';
        window.onclick = (event) => {
            if (event.target == modal) {
                modal.style.display = 'none';
            }
        }

        // Lógica do Formulário (CRIAR NOVO - POST ou EDITAR EXISTENTE - PUT)
        musicoForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            const musicoIdInput = document.getElementById('musico-id');
            const musicoId = musicoIdInput.value;

            // 1. Apanha os dados do formulário, incluindo o checkbox 'ativo'
            const musicoData = {
                nome: document.getElementById('musico-nome').value,
                instrumento: document.getElementById('musico-instrumento').value,
                ativo: document.getElementById('musico-ativo').checked // Lê o estado do checkbox
            };

            const isEditing = musicoId !== '';
            const method = isEditing ? 'PUT' : 'POST';
            const url = isEditing ? `${API_BASE_URL}/musicos/${musicoId}` : `${API_BASE_URL}/musicos`;

            console.log(`Salvando músico: ID=${musicoId || 'Novo'}, Método=${method}, URL=${url}`);

            try {
                // 2. Envia os dados para o MusicoServlet (doPost ou doPut)
                const response = await fetch(url, {
                    method: method,
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(musicoData)
                });

                if (!response.ok) {
                    const errorText = await response.text();
                    throw new Error(`Falha ao salvar músico (Status: ${response.status}): ${errorText}`);
                }

                // 3. Se correu bem, fecha o modal e recarrega a lista
                modal.style.display = 'none';
                musicoIdInput.value = '';
                loadMusicos(); // Chama a função para recarregar a tabela de músicos

            } catch (error) {
                console.error("Erro ao salvar músico:", error);
                alert(`Erro ao salvar. Verifique a consola.\nDetalhe: ${error.message}`);
            }
        });

        // Carrega os dados quando a página abre
        loadMusicos(); // Chama a função para carregar a tabela de músicos
    }

  // --- NOVA LÓGICA PARA A PÁGINA 'agenda.html' ---
    if (window.location.pathname.endsWith('agenda.html')) {
        console.log("Página agenda.html carregada.");
        const modal = document.getElementById('agenda-modal');
        const addBtn = document.getElementById('add-agenda-btn');
        // Certifique-se que o closeBtn é procurado DENTRO do modal correto
        const closeBtn = modal.querySelector('.close-btn');
        const agendaForm = document.getElementById('agenda-form');

        // Lógica do Modal
        addBtn.onclick = () => {
            // Garante que o título do modal está correto ao adicionar
            modal.querySelector('#modal-title').innerText = 'Adicionar Novo Evento';
            agendaForm.reset();
            document.getElementById('agenda-id').value = ''; // Limpa o ID
            modal.style.display = 'flex';
        }
        // Garante que o botão fechar funciona
        if (closeBtn) {
            closeBtn.onclick = () => modal.style.display = 'none';
        } else {
            console.error("Botão Fechar não encontrado no modal da agenda!");
        }
        window.onclick = (event) => {
            if (event.target == modal) {
                modal.style.display = 'none';
            }
        }

        // Lógica do Formulário (CRIAR NOVO - POST ou EDITAR EXISTENTE - PUT)
        agendaForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            const agendaIdInput = document.getElementById('agenda-id');
            const agendaId = agendaIdInput.value;

            // 1. Apanha os dados do formulário
            const agendaData = {
                // O input type="date" já devolve a data no formato yyyy-MM-dd
                data: document.getElementById('agenda-data').value,
                local: document.getElementById('agenda-local').value,
                descricao: document.getElementById('agenda-descricao').value
            };

            // Validação simples da data (não pode estar vazia)
            if (!agendaData.data) {
                alert("Por favor, selecione uma data.");
                return;
            }

            const isEditing = agendaId !== '';
            const method = isEditing ? 'PUT' : 'POST';
            const url = isEditing ? `${API_BASE_URL}/agenda/${agendaId}` : `${API_BASE_URL}/agenda`;

            console.log(`Salvando evento: ID=${agendaId || 'Novo'}, Método=${method}, URL=${url}`, agendaData);

            try {
                // 2. Envia os dados para o AgendaServlet
                const response = await fetch(url, {
                    method: method,
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(agendaData)
                });

                if (!response.ok) {
                    const errorText = await response.text();
                    // Tenta analisar a resposta de erro como JSON, se possível
                    let errorDetail = errorText;
                    try {
                        const errorJson = JSON.parse(errorText);
                        errorDetail = errorJson.message || errorText;
                    } catch (parseError) { /* Ignora se não for JSON */ }
                    throw new Error(`Falha ao salvar evento (Status: ${response.status}): ${errorDetail}`);
                }

                // 3. Se correu bem, fecha o modal e recarrega a lista
                modal.style.display = 'none';
                agendaIdInput.value = '';
                loadAgenda(); // Chama a função para recarregar a tabela da agenda

            } catch (error) {
                console.error("Erro ao salvar evento:", error);
                alert(`Erro ao salvar. Verifique a consola.\nDetalhe: ${error.message}`);
            }
        });

        // Carrega os dados quando a página abre
        loadAgenda(); // Chama a função para carregar a tabela da agenda
    }

    // --- NOVA LÓGICA PARA A PÁGINA 'presencas.html' ---
    if (window.location.pathname.endsWith('presencas.html')) {
        console.log("Página presencas.html carregada.");
        const agendaSelect = document.getElementById('agenda-select');
        const loadCoristasBtn = document.getElementById('load-coristas-presenca-btn');
        const coristasListDiv = document.getElementById('coristas-presenca-list');
        const savePresencasBtn = document.getElementById('save-presencas-btn');

        // 1. Carregar os eventos da agenda no dropdown quando a página carrega
        loadAgendaOptions(); // Chama a função global para preencher o select

        // 2. Adicionar evento ao botão "Carregar Lista de Coristas"
        loadCoristasBtn.addEventListener('click', () => {
            const selectedAgendaId = agendaSelect.value;
            if (!selectedAgendaId) {
                alert("Por favor, selecione um evento da agenda primeiro.");
                return;
            }
            console.log("Carregando coristas para o evento ID:", selectedAgendaId);
            loadCoristasForPresenca(selectedAgendaId); // Chama a função global
        });

        // 3. Adicionar evento ao botão "Salvar Presenças"
        savePresencasBtn.addEventListener('click', async () => {
            const selectedAgendaId = agendaSelect.value;
            if (!selectedAgendaId) {
                alert("Por favor, selecione um evento da agenda.");
                return;
            }

            // Seleciona todos os checkboxes dentro da lista de coristas
            const checkboxes = coristasListDiv.querySelectorAll('input[type="checkbox"]');
            if (checkboxes.length === 0) {
                alert("Nenhum corista carregado para marcar presença.");
                return;
            }

            console.log("Salvando presenças para o evento ID:", selectedAgendaId);
            await savePresencas(selectedAgendaId, checkboxes); // Chama a função global
        });
    }

    // --- NOVA LÓGICA PARA A PÁGINA 'relatorios.html' ---
    if (window.location.pathname.endsWith('relatorios.html')) {
        console.log("Página relatorios.html carregada.");
        const relatorioForm = document.getElementById('relatorio-form');
        const dataInicioInput = document.getElementById('data-inicio');
        const dataFimInput = document.getElementById('data-fim');
        const resultadoDiv = document.getElementById('relatorio-resultado'); // Div onde a tabela será mostrada

        // Adiciona listener para a submissão do formulário
        relatorioForm.addEventListener('submit', async (e) => {
            e.preventDefault(); // Impede o recarregamento da página

            const dataInicio = dataInicioInput.value;
            const dataFim = dataFimInput.value;

            // Validação simples
            if (!dataInicio || !dataFim) {
                alert("Por favor, selecione a Data de Início e a Data de Fim.");
                return;
            }

            // Garante que a data de início não é posterior à data de fim
            if (dataInicio > dataFim) {
                alert("A Data de Início não pode ser posterior à Data de Fim.");
                return;
            }

            console.log(`Gerando relatório de presença de ${dataInicio} a ${dataFim}`);
            // Mostra feedback visual enquanto carrega
            resultadoDiv.querySelector('#relatorio-table-body').innerHTML =
                '<tr><td colspan="4" style="text-align: center;">Gerando relatório...</td></tr>';

            // Chama a função global para buscar e renderizar o relatório
            await loadRelatorioPresenca(dataInicio, dataFim);
        });
    }

});

/**
 * Funções da API (agora são reais e globais)
 */

async function loadCoristas() {
    const tableBody = document.getElementById('coristas-table-body');
    if (!tableBody) return;

    // CHAMADA REAL À API (CoristaServlet - doGet)
    try {
        const response = await fetch(`${API_BASE_URL}/coristas`); // Usa a variável global
        if (!response.ok) {
           throw new Error(`HTTP error! status: ${response.status}`);
        }
        const coristas = await response.json();
        renderCoristas(coristas);
    } catch (error) {
        console.error("Erro ao buscar coristas:", error);
        tableBody.innerHTML = '<tr><td colspan="3">Erro ao carregar dados. O back-end está a rodar?</td></tr>'; // Colspan 3 agora
    }
}

function renderCoristas(coristas) {
    const tableBody = document.getElementById('coristas-table-body');
    tableBody.innerHTML = '';

    if (!coristas || coristas.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="3">Nenhum corista encontrado.</td></tr>'; // Colspan 3 agora
        return;
    }

    coristas.forEach(corista => {
        // Agora só temos 3 colunas (Nome, Naipe, Ações)
        const row = `
            <tr>
                <td>${corista.nome || ''}</td>
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

// Função chamada pelo botão "Editar"
async function editCorista(id) {
    console.log(`Editando corista com ID: ${id}`);
    const modal = document.getElementById('corista-modal');
    const modalTitle = document.getElementById('modal-title');
    const coristaForm = document.getElementById('corista-form');
    const coristaIdInput = document.getElementById('corista-id');
    const nomeInput = document.getElementById('nome');
    const naipeInput = document.getElementById('naipe'); // Lembre-se que 'naipe' no HTML corresponde a 'tipoVoz'

    try {
        // 1. Buscar os dados do corista específico no back-end (SIMULADO)
        //    *** POR AGORA, VAMOS SIMULAR A BUSCA ***
        //    (Idealmente, implementar GET /api/coristas/{id} no Servlet)
        console.log("Simulação: Buscando dados para ID " + id);
        // Encontra o corista na lista já carregada (solução temporária)
        const coristasCarregados = await fetch(`${API_BASE_URL}/coristas`).then(res => res.json()); // Usa a global
        const corista = coristasCarregados.find(c => c.id === id);
        if (!corista) {
             alert("Erro na simulação: Corista não encontrado na lista.");
             return;
        }
        // *** FIM DA SIMULAÇÃO ***

        // 2. Preencher o formulário no modal
        modalTitle.innerText = 'Editar Corista';
        coristaIdInput.value = corista.id; // Guarda o ID no campo escondido
        nomeInput.value = corista.nome;
        naipeInput.value = corista.tipoVoz || ''; // Preenche o naipe/tipoVoz

        // 3. Abrir o modal
        modal.style.display = 'flex';

    } catch (error) {
        console.error("Erro ao preparar edição:", error);
        alert("Erro ao carregar dados para edição. Verifique a consola.");
    }
}

// Função chamada pelo botão "Excluir"
async function deleteCorista(id) {
    if (confirm(`Tem certeza que deseja excluir o corista com ID: ${id}?`)) {

        // CHAMADA REAL À API (CoristaServlet - doDelete)
        try {
            const response = await fetch(`${API_BASE_URL}/coristas/${id}`, { // Usa a global
                method: 'DELETE'
            });

            if (!response.ok) {
                 const errorText = await response.text();
                 throw new Error(`Falha ao apagar corista (Status: ${response.status}): ${errorText}`);
            }

            // Recarrega a lista
            loadCoristas();

        } catch (error) {
            console.error("Erro ao apagar corista:", error);
            alert(`Erro ao apagar. Verifique a consola.\nDetalhe: ${error.message}`);
        }
    }
}

/**
 * Funções da API para Músicos (globais)
 */

async function loadMusicos() {
    const tableBody = document.getElementById('musicos-table-body');
    if (!tableBody) return;

    try {
        // Chama o MusicoServlet (doGet)
        const response = await fetch(`${API_BASE_URL}/musicos`);
        if (!response.ok) {
           throw new Error(`HTTP error! status: ${response.status}`);
        }
        const musicos = await response.json();
        renderMusicos(musicos);
    } catch (error) {
        console.error("Erro ao buscar músicos:", error);
        tableBody.innerHTML = '<tr><td colspan="4">Erro ao carregar dados. O back-end está a rodar?</td></tr>';
    }
}

function renderMusicos(musicos) {
    const tableBody = document.getElementById('musicos-table-body');
    tableBody.innerHTML = '';

    if (!musicos || musicos.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="4">Nenhum músico encontrado.</td></tr>';
        return;
    }

    musicos.forEach(musico => {
        // Renderiza Nome, Instrumento, Ativo (Sim/Não) e Ações
        const row = `
            <tr>
                <td>${musico.nome || ''}</td>
                <td>${musico.instrumento || ''}</td>
                <td>${musico.ativo ? 'Sim' : 'Não'}</td>
                <td>
                    <button class="edit-btn" onclick="editMusico(${musico.id})">Editar</button>
                    <button class="delete-btn" onclick="deleteMusico(${musico.id})">Excluir</button>
                </td>
            </tr>
        `;
        tableBody.innerHTML += row;
    });
}

// Função chamada pelo botão "Editar" (para músicos)
async function editMusico(id) {
    console.log(`Editando músico com ID: ${id}`);
    const modal = document.getElementById('musico-modal');
    const modalTitle = document.getElementById('modal-title');
    const musicoForm = document.getElementById('musico-form');
    const musicoIdInput = document.getElementById('musico-id');
    const nomeInput = document.getElementById('musico-nome');
    const instrumentoInput = document.getElementById('musico-instrumento');
    const ativoCheckbox = document.getElementById('musico-ativo');

    try {
        // SIMULAÇÃO da busca (igual à dos coristas)
        console.log("Simulação: Buscando dados do músico ID " + id);
        const musicosCarregados = await fetch(`${API_BASE_URL}/musicos`).then(res => res.json());
        const musico = musicosCarregados.find(m => m.id === id);
        if (!musico) {
             alert("Erro na simulação: Músico não encontrado na lista.");
             return;
        }
        // FIM DA SIMULAÇÃO

        // Preenche o formulário
        modalTitle.innerText = 'Editar Músico';
        musicoIdInput.value = musico.id;
        nomeInput.value = musico.nome;
        instrumentoInput.value = musico.instrumento || '';
        ativoCheckbox.checked = musico.ativo; // Define o estado do checkbox

        // Abre o modal
        modal.style.display = 'flex';

    } catch (error) {
        console.error("Erro ao preparar edição do músico:", error);
        alert("Erro ao carregar dados para edição. Verifique a consola.");
    }
}

// Função chamada pelo botão "Excluir" (para músicos)
async function deleteMusico(id) {
    if (confirm(`Tem certeza que deseja excluir o músico com ID: ${id}?`)) {
        try {
            // Chama o MusicoServlet (doDelete)
            const response = await fetch(`${API_BASE_URL}/musicos/${id}`, {
                method: 'DELETE'
            });

            if (!response.ok) {
                 const errorText = await response.text();
                 throw new Error(`Falha ao apagar músico (Status: ${response.status}): ${errorText}`);
            }
            loadMusicos(); // Recarrega a lista
        } catch (error) {
            console.error("Erro ao apagar músico:", error);
            alert(`Erro ao apagar. Verifique a consola.\nDetalhe: ${error.message}`);
        }
    }
}

/**
 * Funções da API para Agenda (globais)
 */

async function loadAgenda() {
    const tableBody = document.getElementById('agenda-table-body');
    if (!tableBody) {
        console.warn("Elemento 'agenda-table-body' não encontrado na página atual.");
        return; // Sai se a tabela não existir nesta página
    }

    try {
        // Chama o AgendaServlet (doGet)
        const response = await fetch(`${API_BASE_URL}/agenda`);
        if (!response.ok) {
           throw new Error(`HTTP error! status: ${response.status}`);
        }
        const agendaItens = await response.json();
        renderAgenda(agendaItens);
    } catch (error) {
        console.error("Erro ao buscar agenda:", error);
        tableBody.innerHTML = '<tr><td colspan="4">Erro ao carregar dados. O back-end está a rodar?</td></tr>';
    }
}

function renderAgenda(agendaItens) {
    const tableBody = document.getElementById('agenda-table-body');
    tableBody.innerHTML = ''; // Limpa a tabela

    if (!agendaItens || agendaItens.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="4">Nenhum evento na agenda encontrado.</td></tr>';
        return;
    }

    // Ordena os eventos por data (mais recentes primeiro) - opcional
    agendaItens.sort((a, b) => (b.data || '').localeCompare(a.data || ''));

    agendaItens.forEach(item => {
        // Formata a data para dd/MM/yyyy para exibição (se existir)
        let dataFormatada = 'N/D';
        if (item.data) {
            try {
                // A data vem como 'yyyy-MM-dd' do backend
                const [ano, mes, dia] = item.data.split('-');
                dataFormatada = `${dia}/${mes}/${ano}`;
            } catch (e) {
                console.error("Erro ao formatar data:", item.data, e);
                dataFormatada = item.data; // Mostra o formato original se falhar
            }
        }

        const row = `
            <tr>
                <td>${dataFormatada}</td>
                <td>${item.local || ''}</td>
                <td>${item.descricao || ''}</td>
                <td>
                    <button class="edit-btn" onclick="editAgenda(${item.id})">Editar</button>
                    <button class="delete-btn" onclick="deleteAgenda(${item.id})">Excluir</button>
                </td>
            </tr>
        `;
        tableBody.innerHTML += row;
    });
}

// Função chamada pelo botão "Editar" (para agenda)
async function editAgenda(id) {
    console.log(`Editando evento da agenda com ID: ${id}`);
    const modal = document.getElementById('agenda-modal');
    // Garante que o título do modal está correto ao editar
    const modalTitle = modal.querySelector('#modal-title');
    const agendaForm = document.getElementById('agenda-form');
    const agendaIdInput = document.getElementById('agenda-id');
    const dataInput = document.getElementById('agenda-data');
    const localInput = document.getElementById('agenda-local');
    const descricaoInput = document.getElementById('agenda-descricao');

    try {
        // SIMULAÇÃO da busca (idealmente, GET /api/agenda/{id})
        console.log("Simulação: Buscando dados do evento ID " + id);
        const agendaCarregada = await fetch(`${API_BASE_URL}/agenda`).then(res => res.json());
        const item = agendaCarregada.find(a => a.id === id);
        if (!item) {
             alert("Erro na simulação: Evento não encontrado na lista.");
             return;
        }
        // FIM DA SIMULAÇÃO

        // Preenche o formulário
        if(modalTitle) modalTitle.innerText = 'Editar Evento'; // Verifica se o elemento existe
        agendaIdInput.value = item.id;
        // O input type="date" espera o formato yyyy-MM-dd, que o backend envia
        dataInput.value = item.data || '';
        localInput.value = item.local || '';
        descricaoInput.value = item.descricao || '';

        // Abre o modal
        modal.style.display = 'flex';

    } catch (error) {
        console.error("Erro ao preparar edição do evento:", error);
        alert("Erro ao carregar dados para edição. Verifique a consola.");
    }
}

// Função chamada pelo botão "Excluir" (para agenda)
async function deleteAgenda(id) {
    if (confirm(`Tem certeza que deseja excluir o evento da agenda com ID: ${id}?`)) {
        try {
            // Chama o AgendaServlet (doDelete)
            const response = await fetch(`${API_BASE_URL}/agenda/${id}`, {
                method: 'DELETE'
            });

            if (!response.ok) {
                 const errorText = await response.text();
                 throw new Error(`Falha ao apagar evento (Status: ${response.status}): ${errorText}`);
            }
            loadAgenda(); // Recarrega a lista
        } catch (error) {
            console.error("Erro ao apagar evento:", error);
            alert(`Erro ao apagar. Verifique a consola.\nDetalhe: ${error.message}`);
        }
    }
}

/**
 * Funções da API para Presenças (globais)
 */

// Carrega os eventos da agenda no <select>
async function loadAgendaOptions() {
    const agendaSelect = document.getElementById('agenda-select');
    if (!agendaSelect) return; // Só executa se o select existir

    try {
        const response = await fetch(`${API_BASE_URL}/agenda`);
        if (!response.ok) throw new Error('Falha ao buscar eventos da agenda');
        const agendaItens = await response.json();

        agendaSelect.innerHTML = '<option value="">-- Selecione um Evento --</option>'; // Opção padrão

        // Ordena por data (mais recentes primeiro)
        agendaItens.sort((a, b) => (b.data || '').localeCompare(a.data || ''));

        agendaItens.forEach(item => {
            // Formata a data para dd/MM/yyyy
            let dataFormatada = 'N/D';
             if (item.data) {
                try {
                    const [ano, mes, dia] = item.data.split('-');
                    dataFormatada = `${dia}/${mes}/${ano}`;
                } catch (e) { dataFormatada = item.data; }
            }
            const optionText = `${dataFormatada} - ${item.local || 'Local não definido'}`;
            const option = new Option(optionText, item.id); // Texto visível, valor = ID do evento
            agendaSelect.add(option);
        });

    } catch (error) {
        console.error("Erro ao carregar opções da agenda:", error);
        agendaSelect.innerHTML = '<option value="">Erro ao carregar eventos</option>';
    }
}

// Carrega a lista de coristas com checkboxes para um evento específico
async function loadCoristasForPresenca(agendaId) {
    const coristasListDiv = document.getElementById('coristas-presenca-list');
    if (!coristasListDiv) return;

    coristasListDiv.innerHTML = '<p>Carregando coristas...</p>'; // Feedback visual

    try {
        // Busca TODOS os coristas (idealmente, poderíamos ter uma API para buscar
        // coristas E o seu estado de presença para este evento específico, mas vamos simplificar)
        const response = await fetch(`${API_BASE_URL}/coristas`);
        if (!response.ok) throw new Error('Falha ao buscar coristas');
        const coristas = await response.json();

        if (!coristas || coristas.length === 0) {
            coristasListDiv.innerHTML = '<p>Nenhum corista encontrado.</p>';
            return;
        }

        coristasListDiv.innerHTML = ''; // Limpa a div

        // Cria um checkbox para cada corista
        coristas.forEach(corista => {
            const div = document.createElement('div');
            div.style.marginBottom = '8px'; // Espaçamento
            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.id = `corista-${corista.id}`;
            checkbox.value = corista.id; // O valor do checkbox é o ID do corista
            checkbox.dataset.coristaId = corista.id; // Guarda o ID no dataset para fácil acesso

            // TODO: Idealmente, deveríamos buscar o estado de presença atual
            // e marcar o checkbox se já estiver presente. Por agora, começam desmarcados.
            checkbox.checked = false;

            const label = document.createElement('label');
            label.htmlFor = `corista-${corista.id}`;
            label.textContent = ` ${corista.nome}`; // Adiciona espaço antes do nome
            label.style.marginLeft = '5px';

            div.appendChild(checkbox);
            div.appendChild(label);
            coristasListDiv.appendChild(div);
        });

    } catch (error) {
        console.error("Erro ao carregar lista de coristas para presença:", error);
        coristasListDiv.innerHTML = '<p>Erro ao carregar coristas.</p>';
    }
}

// Salva o estado de presença para todos os coristas listados
async function savePresencas(agendaId, checkboxes) {
    console.log("Iniciando salvamento de presenças...");
    let successCount = 0;
    let errorCount = 0;

    // Itera sobre cada checkbox e envia um pedido POST para o PresencaServlet
    // Usamos Promise.all para enviar os pedidos em paralelo (mais rápido)
    const savePromises = [];
    checkboxes.forEach(checkbox => {
        const presencaData = {
            idCorista: parseInt(checkbox.dataset.coristaId), // Pega o ID do dataset
            idAgenda: parseInt(agendaId),
            presente: checkbox.checked // Pega o estado atual (marcado/desmarcado)
        };

        console.log("Enviando:", presencaData);

        const savePromise = fetch(`${API_BASE_URL}/presencas`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(presencaData)
        })
        .then(response => {
            if (!response.ok) {
                // Se a resposta não for OK, tentamos ler o erro e rejeitamos a promise
                return response.text().then(text => {
                   console.error(`Erro ao salvar presença para corista ID ${presencaData.idCorista}: Status ${response.status}, Resposta: ${text}`);
                   throw new Error(`Falha ao salvar presença para ID ${presencaData.idCorista}`);
                });
            }
            // Se for OK, contamos como sucesso
            successCount++;
            return response.json(); // Ou apenas response.text() se a resposta for simples
        })
        .catch(error => {
            // Apanha erros de rede ou os erros lançados acima
            errorCount++;
            console.error(`Erro na chamada fetch para corista ID ${presencaData.idCorista}:`, error);
            // Retornamos um objeto de erro para que Promise.all não pare nos erros
            return { error: true, coristaId: presencaData.idCorista, message: error.message };
        });
        savePromises.push(savePromise);
    });

    try {
        // Espera que *todos* os pedidos terminem (seja com sucesso ou erro tratado)
        const results = await Promise.all(savePromises);
        console.log("Resultados do salvamento:", results);

        // Monta a mensagem final
        let finalMessage = `${successCount} presenças salvas com sucesso.`;
        if (errorCount > 0) {
            finalMessage += ` ${errorCount} falharam. Verifique a consola para detalhes.`;
            alert(finalMessage); // Mostra alerta se houve erros
        } else {
            alert(finalMessage); // Mostra alerta de sucesso total
        }

        // Opcional: Recarregar a lista ou limpar, dependendo do fluxo desejado
        // loadCoristasForPresenca(agendaId);

    } catch (error) {
        // Este catch é para erros inesperados no Promise.all, embora tenhamos tratado erros individuais
        console.error("Erro inesperado ao salvar presenças:", error);
        alert("Ocorreu um erro inesperado ao salvar todas as presenças.");
    }
}
/**
 * Funções da API para Relatórios (globais)
 */

async function loadRelatorioPresenca(dataInicio, dataFim) {
    const tableBody = document.getElementById('relatorio-table-body');
    if (!tableBody) {
        console.error("Elemento 'relatorio-table-body' não encontrado.");
        return;
    }

    try {
        // Constrói a URL com os parâmetros de data
        const url = `${API_BASE_URL}/relatorios/presenca?dataInicio=${encodeURIComponent(dataInicio)}&dataFim=${encodeURIComponent(dataFim)}`;
        console.log("Chamando API:", url);

        // Chama o RelatorioServlet (doGet)
        const response = await fetch(url);

        if (!response.ok) {
            let errorMsg = `Erro ${response.status}: ${response.statusText}`;
            try {
                // Tenta ler uma mensagem de erro mais detalhada do corpo da resposta
                const errorBody = await response.text();
                errorMsg = `Erro ${response.status}: ${errorBody || response.statusText}`;
            } catch (e) { /* Ignora se não conseguir ler o corpo */ }
            throw new Error(errorMsg);
        }

        const presencas = await response.json();
        renderRelatorioPresenca(presencas);

    } catch (error) {
        console.error("Erro ao buscar relatório de presença:", error);
        tableBody.innerHTML = `<tr><td colspan="4" style="color: red; text-align: center;">Erro ao gerar relatório: ${error.message}</td></tr>`;
    }
}

function renderRelatorioPresenca(presencas) {
    const tableBody = document.getElementById('relatorio-table-body');
    tableBody.innerHTML = ''; // Limpa a tabela

    if (!presencas || presencas.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="4" style="text-align: center;">Nenhum registo de presença encontrado para o período selecionado.</td></tr>';
        return;
    }

    // O DAO já ordena os dados, mas poderíamos reordenar aqui se necessário

    presencas.forEach(presenca => {
        // O JSON retornado pelo Servlet deve incluir os objetos aninhados
        // Corista e Agenda, graças ao JOIN FETCH no DAO.
        const coristaNome = presenca.corista ? presenca.corista.nome : 'Corista Desconhecido';
        const agendaData = presenca.agenda ? presenca.agenda.data : 'N/D';
        const agendaLocal = presenca.agenda ? presenca.agenda.local : 'N/D';
        const presenteStatus = presenca.presente ? 'Sim' : 'Não';

        // Formata a data para dd/MM/yyyy
        let dataFormatada = 'N/D';
        if (agendaData && agendaData !== 'N/D') {
            try {
                const [ano, mes, dia] = agendaData.split('-');
                dataFormatada = `${dia}/${mes}/${ano}`;
            } catch (e) { dataFormatada = agendaData; }
        }

        const row = `
            <tr>
                <td>${dataFormatada}</td>
                <td>${agendaLocal || ''}</td>
                <td>${coristaNome || ''}</td>
                <td>${presenteStatus}</td>
            </tr>
        `;
        tableBody.innerHTML += row;
    });
}