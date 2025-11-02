
const API_BASE_URL = '/gestao-coral-backend/api';

document.addEventListener('DOMContentLoaded', () => {
    
    const loginForm = document.getElementById('login-form');
    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => { 
            e.preventDefault();

            const usernameInput = document.getElementById('username');
            const passwordInput = document.getElementById('password');

            const loginData = {
                username: usernameInput.value,
                password: passwordInput.value
            };

            try {
                
                const response = await fetch(`${API_BASE_URL}/auth/login`, { 
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(loginData)
                });

                const result = await response.json(); 

                if (response.ok && result.success) {
                    
                    console.log("Login real com sucesso para:", result.username);
                    window.location.href = 'dashboard.html';
                } else {
                    
                    alert(result.message || 'Falha no login.');
                    passwordInput.value = ''; 
                }

            } catch (error) {
                console.error("Erro durante o login:", error);
                alert("Erro ao tentar fazer login. Verifique a consola.");
            }
        });
    }
    
    const logoutBtn = document.getElementById('logout-btn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', () => {
            window.location.href = 'index.html'; 
        });
    }

    
    if (window.location.pathname.endsWith('coristas.html')) {
        const modal = document.getElementById('corista-modal');
        const addBtn = document.getElementById('add-corista-btn');
        const closeBtn = document.querySelector('.close-btn');
        const coristaForm = document.getElementById('corista-form');

        
        addBtn.onclick = () => {
            document.getElementById('modal-title').innerText = 'Adicionar Novo Corista';
            coristaForm.reset();
            document.getElementById('corista-id').value = ''; 
            modal.style.display = 'flex';
        }
        closeBtn.onclick = () => modal.style.display = 'none';
        window.onclick = (event) => {
            if (event.target == modal) {
                modal.style.display = 'none';
            }
        }
    
coristaForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const coristaIdInput = document.getElementById('corista-id');
        const coristaId = coristaIdInput.value;

        
        const coristaData = {
            nome: document.getElementById('nome').value,
            tipoVoz: document.getElementById('naipe').value, 
            ativo: document.getElementById('corista-ativo').checked 
        };

        const isEditing = coristaId !== '';
        const method = isEditing ? 'PUT' : 'POST';
        const url = isEditing ? `${API_BASE_URL}/coristas/${coristaId}` : `${API_BASE_URL}/coristas`;

        console.log(`Salvando corista: ID=${coristaId || 'Novo'}, Método=${method}, URL=${url}`, coristaData);

        try {
            
            const response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(coristaData)
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Falha ao salvar corista (Status: ${response.status}): ${errorText}`);
            }

            
            modal.style.display = 'none';
            coristaIdInput.value = '';
            loadCoristas();

        } catch (error) {
            console.error("Erro ao salvar corista:", error);
            alert(`Erro ao salvar. Verifique a consola.\nDetalhe: ${error.message}`);
        }
    });

        
        loadCoristas();
    }

    
    if (window.location.pathname.endsWith('musicos.html')) {
        console.log("Página musicos.html carregada."); 
        const modal = document.getElementById('musico-modal');
        const addBtn = document.getElementById('add-musico-btn');
        const closeBtn = modal.querySelector('.close-btn'); 
        const musicoForm = document.getElementById('musico-form');

        
        addBtn.onclick = () => {
            document.getElementById('modal-title').innerText = 'Adicionar Novo Músico';
            musicoForm.reset();
            document.getElementById('musico-id').value = ''; 
            document.getElementById('musico-ativo').checked = true; 
            modal.style.display = 'flex';
        }
        closeBtn.onclick = () => modal.style.display = 'none';
        window.onclick = (event) => {
            if (event.target == modal) {
                modal.style.display = 'none';
            }
        }

        
        musicoForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            const musicoIdInput = document.getElementById('musico-id');
            const musicoId = musicoIdInput.value;

            
            const musicoData = {
                nome: document.getElementById('musico-nome').value,
                instrumento: document.getElementById('musico-instrumento').value,
                ativo: document.getElementById('musico-ativo').checked 
            };

            const isEditing = musicoId !== '';
            const method = isEditing ? 'PUT' : 'POST';
            const url = isEditing ? `${API_BASE_URL}/musicos/${musicoId}` : `${API_BASE_URL}/musicos`;

            console.log(`Salvando músico: ID=${musicoId || 'Novo'}, Método=${method}, URL=${url}`);

            try {
                
                const response = await fetch(url, {
                    method: method,
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(musicoData)
                });

                if (!response.ok) {
                    const errorText = await response.text();
                    throw new Error(`Falha ao salvar músico (Status: ${response.status}): ${errorText}`);
                }

                
                modal.style.display = 'none';
                musicoIdInput.value = '';
                loadMusicos(); 

            } catch (error) {
                console.error("Erro ao salvar músico:", error);
                alert(`Erro ao salvar. Verifique a consola.\nDetalhe: ${error.message}`);
            }
        });

        
        loadMusicos(); 
    }

  
    if (window.location.pathname.endsWith('agenda.html')) {
        console.log("Página agenda.html carregada.");
        const modal = document.getElementById('agenda-modal');
        const addBtn = document.getElementById('add-agenda-btn');
        
        const closeBtn = modal.querySelector('.close-btn');
        const agendaForm = document.getElementById('agenda-form');

        
        addBtn.onclick = () => {
            
            modal.querySelector('#modal-title').innerText = 'Adicionar Novo Evento';
            agendaForm.reset();
            document.getElementById('agenda-id').value = ''; 
            modal.style.display = 'flex';
        }
        
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

        
        agendaForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            const agendaIdInput = document.getElementById('agenda-id');
            const agendaId = agendaIdInput.value;

            
            const agendaData = {
                
                data: document.getElementById('agenda-data').value,
                local: document.getElementById('agenda-local').value,
                descricao: document.getElementById('agenda-descricao').value
            };

            
            if (!agendaData.data) {
                alert("Por favor, selecione uma data.");
                return;
            }

            const isEditing = agendaId !== '';
            const method = isEditing ? 'PUT' : 'POST';
            const url = isEditing ? `${API_BASE_URL}/agenda/${agendaId}` : `${API_BASE_URL}/agenda`;

            console.log(`Salvando evento: ID=${agendaId || 'Novo'}, Método=${method}, URL=${url}`, agendaData);

            try {
                
                const response = await fetch(url, {
                    method: method,
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(agendaData)
                });

                if (!response.ok) {
                    const errorText = await response.text();
                    
                    let errorDetail = errorText;
                    try {
                        const errorJson = JSON.parse(errorText);
                        errorDetail = errorJson.message || errorText;
                    } catch (parseError) {  }
                    throw new Error(`Falha ao salvar evento (Status: ${response.status}): ${errorDetail}`);
                }

                
                modal.style.display = 'none';
                agendaIdInput.value = '';
                loadAgenda(); 

            } catch (error) {
                console.error("Erro ao salvar evento:", error);
                alert(`Erro ao salvar. Verifique a consola.\nDetalhe: ${error.message}`);
            }
        });

        
        loadAgenda(); 
    }

    
 if (window.location.pathname.endsWith('presencas.html')) {
        console.log("Página presencas.html carregada.");
        const agendaSelect = document.getElementById('agenda-select');
        const loadListasBtn = document.getElementById('load-listas-presenca-btn');
        const coristasListDiv = document.getElementById('coristas-presenca-list');
        const musicosListDiv = document.getElementById('musicos-presenca-list');
        const savePresencasBtn = document.getElementById('save-presencas-btn');

        loadAgendaOptions();

        loadListasBtn.addEventListener('click', () => {
            const selectedAgendaId = agendaSelect.value;
            if (!selectedAgendaId) {
                alert("Por favor, selecione um evento da agenda primeiro.");
                return;
            }
            console.log("Carregando listas para o evento ID:", selectedAgendaId);
            loadCoristasForPresenca(selectedAgendaId);
            loadMusicosForPresenca(selectedAgendaId);
        });

        savePresencasBtn.addEventListener('click', async () => {
            const selectedAgendaId = agendaSelect.value;
            if (!selectedAgendaId) {
                alert("Por favor, selecione um evento da agenda.");
                return;
            }

            const coristaCheckboxes = coristasListDiv.querySelectorAll('input[type="checkbox"]');
            const musicoCheckboxes = musicosListDiv.querySelectorAll('input[type="checkbox"]');

            if (coristaCheckboxes.length === 0 && musicoCheckboxes.length === 0) {
                alert("Nenhuma lista carregada para marcar presença.");
                return;
            }

            console.log("Salvando presenças para o evento ID:", selectedAgendaId);
            await saveTodasPresencas(selectedAgendaId, coristaCheckboxes, musicoCheckboxes);
        });
    }
    
    if (window.location.pathname.endsWith('relatorios.html')) {
        console.log("Página relatorios.html carregada.");
        const relatorioForm = document.getElementById('relatorio-form');
        const dataInicioInput = document.getElementById('data-inicio');
        const dataFimInput = document.getElementById('data-fim');
        const resultadoDiv = document.getElementById('relatorio-resultado'); 

        
        relatorioForm.addEventListener('submit', async (e) => {
            e.preventDefault(); 

            const dataInicio = dataInicioInput.value;
            const dataFim = dataFimInput.value;

            
            if (!dataInicio || !dataFim) {
                alert("Por favor, selecione a Data de Início e a Data de Fim.");
                return;
            }

            
            if (dataInicio > dataFim) {
                alert("A Data de Início não pode ser posterior à Data de Fim.");
                return;
            }

            console.log(`Gerando relatório de presença de ${dataInicio} a ${dataFim}`);
            
            resultadoDiv.querySelector('#relatorio-table-body').innerHTML =
                '<tr><td colspan="4" style="text-align: center;">Gerando relatório...</td></tr>';

            
            await loadRelatorioPresenca(dataInicio, dataFim);
        });
    }

    
    if (window.location.pathname.endsWith('dashboard.html') || window.location.pathname.endsWith('/gestao-coral-backend/')) { 
        console.log("Página dashboard.html carregada.");
        
        loadDashboardData();
    }

});

async function loadCoristas() {
    const tableBody = document.getElementById('coristas-table-body');
    if (!tableBody) return;

    
    try {
        const response = await fetch(`${API_BASE_URL}/coristas`); 
        if (!response.ok) {
           throw new Error(`HTTP error! status: ${response.status}`);
        }
        const coristas = await response.json();
        renderCoristas(coristas);
    } catch (error) {
        console.error("Erro ao buscar coristas:", error);
        tableBody.innerHTML = '<tr><td colspan="3">Erro ao carregar dados. O back-end está a rodar?</td></tr>'; 
    }
}

function renderCoristas(coristas) {
    const tableBody = document.getElementById('coristas-table-body');
    tableBody.innerHTML = '';

    if (!coristas || coristas.length === 0) {
        
        tableBody.innerHTML = '<tr><td colspan="4">Nenhum corista encontrado.</td></tr>';
        return;
    }

   coristas.forEach(corista => {
    
    const row = `
        <tr>
            <td>${corista.nome || ''}</td>
            <td>${corista.tipoVoz || ''}</td>
            <td>${corista.ativo ? 'Sim' : 'Não'}</td> 
            <td>
                <button class="edit-btn" onclick="editCorista(${corista.id})">Editar</button>
                <button class="delete-btn" onclick="deleteCorista(${corista.id})">Excluir</button>
            </td>
        </tr>
    `;
    tableBody.innerHTML += row;
});
}


async function editCorista(id) {
    console.log(`Editando corista com ID: ${id}`);
    const modal = document.getElementById('corista-modal');
    const modalTitle = modal.querySelector('#modal-title'); 
    const coristaForm = document.getElementById('corista-form');
    const coristaIdInput = document.getElementById('corista-id');
    const nomeInput = document.getElementById('nome');
    const naipeInput = document.getElementById('naipe'); 
    const ativoCheckbox = document.getElementById('corista-ativo'); 

    try {
        
        console.log("Simulação: Buscando dados para ID " + id);
        const coristasCarregados = await fetch(`${API_BASE_URL}/coristas`).then(res => res.json());
        const corista = coristasCarregados.find(c => c.id === id);
        if (!corista) {
             alert("Erro na simulação: Corista não encontrado na lista.");
             return;
        }
        

        
        if (modalTitle) modalTitle.innerText = 'Editar Corista';
        coristaIdInput.value = corista.id;
        nomeInput.value = corista.nome;
        naipeInput.value = corista.tipoVoz || '';
        ativoCheckbox.checked = corista.ativo; 

        
        modal.style.display = 'flex';

    } catch (error) {
        console.error("Erro ao preparar edição:", error);
        alert("Erro ao carregar dados para edição. Verifique a consola.");
    }
}


async function deleteCorista(id) {
    if (confirm(`Tem certeza que deseja excluir o corista com ID: ${id}?`)) {

        
        try {
            const response = await fetch(`${API_BASE_URL}/coristas/${id}`, { 
                method: 'DELETE'
            });

            if (!response.ok) {
                 const errorText = await response.text();
                 throw new Error(`Falha ao apagar corista (Status: ${response.status}): ${errorText}`);
            }

            
            loadCoristas();

        } catch (error) {
            console.error("Erro ao apagar corista:", error);
            alert(`Erro ao apagar. Verifique a consola.\nDetalhe: ${error.message}`);
        }
    }
}

async function loadMusicos() {
    const tableBody = document.getElementById('musicos-table-body');
    if (!tableBody) return;

    try {
        
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
        
        console.log("Simulação: Buscando dados do músico ID " + id);
        const musicosCarregados = await fetch(`${API_BASE_URL}/musicos`).then(res => res.json());
        const musico = musicosCarregados.find(m => m.id === id);
        if (!musico) {
             alert("Erro na simulação: Músico não encontrado na lista.");
             return;
        }
        

        
        modalTitle.innerText = 'Editar Músico';
        musicoIdInput.value = musico.id;
        nomeInput.value = musico.nome;
        instrumentoInput.value = musico.instrumento || '';
        ativoCheckbox.checked = musico.ativo; 

        
        modal.style.display = 'flex';

    } catch (error) {
        console.error("Erro ao preparar edição do músico:", error);
        alert("Erro ao carregar dados para edição. Verifique a consola.");
    }
}


async function deleteMusico(id) {
    if (confirm(`Tem certeza que deseja excluir o músico com ID: ${id}?`)) {
        try {
            
            const response = await fetch(`${API_BASE_URL}/musicos/${id}`, {
                method: 'DELETE'
            });

            if (!response.ok) {
                 const errorText = await response.text();
                 throw new Error(`Falha ao apagar músico (Status: ${response.status}): ${errorText}`);
            }
            loadMusicos(); 
        } catch (error) {
            console.error("Erro ao apagar músico:", error);
            alert(`Erro ao apagar. Verifique a consola.\nDetalhe: ${error.message}`);
        }
    }
}

async function loadAgenda() {
    const tableBody = document.getElementById('agenda-table-body');
    if (!tableBody) {
        console.warn("Elemento 'agenda-table-body' não encontrado na página atual.");
        return; 
    }

    try {
        
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
    tableBody.innerHTML = ''; 

    if (!agendaItens || agendaItens.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="4">Nenhum evento na agenda encontrado.</td></tr>';
        return;
    }

    
    agendaItens.sort((a, b) => (b.data || '').localeCompare(a.data || ''));

    agendaItens.forEach(item => {
        
        let dataFormatada = 'N/D';
        if (item.data) {
            try {
                
                const [ano, mes, dia] = item.data.split('-');
                dataFormatada = `${dia}/${mes}/${ano}`;
            } catch (e) {
                console.error("Erro ao formatar data:", item.data, e);
                dataFormatada = item.data; 
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


async function editAgenda(id) {
    console.log(`Editando evento da agenda com ID: ${id}`);
    const modal = document.getElementById('agenda-modal');
    
    const modalTitle = modal.querySelector('#modal-title');
    const agendaForm = document.getElementById('agenda-form');
    const agendaIdInput = document.getElementById('agenda-id');
    const dataInput = document.getElementById('agenda-data');
    const localInput = document.getElementById('agenda-local');
    const descricaoInput = document.getElementById('agenda-descricao');

    try {
        
        console.log("Simulação: Buscando dados do evento ID " + id);
        const agendaCarregada = await fetch(`${API_BASE_URL}/agenda`).then(res => res.json());
        const item = agendaCarregada.find(a => a.id === id);
        if (!item) {
             alert("Erro na simulação: Evento não encontrado na lista.");
             return;
        }
        

        
        if(modalTitle) modalTitle.innerText = 'Editar Evento'; 
        agendaIdInput.value = item.id;
        
        dataInput.value = item.data || '';
        localInput.value = item.local || '';
        descricaoInput.value = item.descricao || '';

        
        modal.style.display = 'flex';

    } catch (error) {
        console.error("Erro ao preparar edição do evento:", error);
        alert("Erro ao carregar dados para edição. Verifique a consola.");
    }
}


async function deleteAgenda(id) {
    if (confirm(`Tem certeza que deseja excluir o evento da agenda com ID: ${id}?`)) {
        try {
            
            const response = await fetch(`${API_BASE_URL}/agenda/${id}`, {
                method: 'DELETE'
            });

            if (!response.ok) {
                 const errorText = await response.text();
                 throw new Error(`Falha ao apagar evento (Status: ${response.status}): ${errorText}`);
            }
            loadAgenda(); 
        } catch (error) {
            console.error("Erro ao apagar evento:", error);
            alert(`Erro ao apagar. Verifique a consola.\nDetalhe: ${error.message}`);
        }
    }
}

async function loadAgendaOptions() {
    const agendaSelect = document.getElementById('agenda-select');
    if (!agendaSelect) return; 

    try {
        const response = await fetch(`${API_BASE_URL}/agenda`);
        if (!response.ok) throw new Error('Falha ao buscar eventos da agenda');
        const agendaItens = await response.json();

        agendaSelect.innerHTML = '<option value="">-- Selecione um Evento --</option>'; 

        
        agendaItens.sort((a, b) => (b.data || '').localeCompare(a.data || ''));

        agendaItens.forEach(item => {
            
            let dataFormatada = 'N/D';
             if (item.data) {
                try {
                    const [ano, mes, dia] = item.data.split('-');
                    dataFormatada = `${dia}/${mes}/${ano}`;
                } catch (e) { dataFormatada = item.data; }
            }
            const optionText = `${dataFormatada} - ${item.local || 'Local não definido'}`;
            const option = new Option(optionText, item.id); 
            agendaSelect.add(option);
        });

    } catch (error) {
        console.error("Erro ao carregar opções da agenda:", error);
        agendaSelect.innerHTML = '<option value="">Erro ao carregar eventos</option>';
    }
}


async function loadCoristasForPresenca(agendaId) {
    const coristasListDiv = document.getElementById('coristas-presenca-list');
    if (!coristasListDiv) return;

    coristasListDiv.innerHTML = '<p>Carregando coristas...</p>'; 

    try {
        
        
        const response = await fetch(`${API_BASE_URL}/coristas`);
        if (!response.ok) throw new Error('Falha ao buscar coristas');
        const coristas = await response.json();

        if (!coristas || coristas.length === 0) {
            coristasListDiv.innerHTML = '<p>Nenhum corista encontrado.</p>';
            return;
        }

        coristasListDiv.innerHTML = ''; 

        
        coristas.forEach(corista => {
            const div = document.createElement('div');
            div.style.marginBottom = '8px'; 
            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.id = `corista-${corista.id}`;
            checkbox.value = corista.id; 
            checkbox.dataset.coristaId = corista.id; 

            
            
            checkbox.checked = false;

            const label = document.createElement('label');
            label.htmlFor = `corista-${corista.id}`;
            label.textContent = ` ${corista.nome}`; 
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


async function saveTodasPresencas(agendaId, coristaCheckboxes, musicoCheckboxes) {
    console.log("Iniciando salvamento de presenças...");
    let successCount = 0;
    let errorCount = 0;
    const savePromises = [];

    coristaCheckboxes.forEach(checkbox => {
        const presencaData = {
            idCorista: parseInt(checkbox.dataset.coristaId),
            idAgenda: parseInt(agendaId),
            presente: checkbox.checked
        };

        const savePromise = fetch(`${API_BASE_URL}/presencas`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(presencaData)
        })
        .then(response => {
            if (!response.ok) {
               errorCount++;
               console.error(`Erro ao salvar presença para corista ID ${presencaData.idCorista}`);
            } else {
               successCount++;
            }
        })
        .catch(error => {
            errorCount++;
            console.error(`Erro na chamada fetch para corista ID ${presencaData.idCorista}:`, error);
        });
        savePromises.push(savePromise);
    });

    musicoCheckboxes.forEach(checkbox => {
        const presencaData = {
            idMusico: parseInt(checkbox.dataset.musicoId),
            idAgenda: parseInt(agendaId),
            presente: checkbox.checked
        };

        const savePromise = fetch(`${API_BASE_URL}/presencas-musicos`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(presencaData)
        })
        .then(response => {
            if (!response.ok) {
               errorCount++;
               console.error(`Erro ao salvar presença para músico ID ${presencaData.idMusico}`);
            } else {
               successCount++;
            }
        })
        .catch(error => {
            errorCount++;
            console.error(`Erro na chamada fetch para músico ID ${presencaData.idMusico}:`, error);
        });
        savePromises.push(savePromise);
    });

    try {
        await Promise.all(savePromises);

        let finalMessage = `${successCount} presenças salvas com sucesso.`;
        if (errorCount > 0) {
            finalMessage += ` ${errorCount} falharam. Verifique a consola.`;
        }
        alert(finalMessage);

    } catch (error) {
        console.error("Erro inesperado ao salvar presenças:", error);
        alert("Ocorreu um erro inesperado ao salvar todas as presenças.");
    }
}

async function loadRelatorioPresenca(dataInicio, dataFim) {
    const tableBody = document.getElementById('relatorio-table-body');
    if (!tableBody) {
        console.error("Elemento 'relatorio-table-body' não encontrado.");
        return;
    }

    try {
        
        const url = `${API_BASE_URL}/relatorios/presenca?dataInicio=${encodeURIComponent(dataInicio)}&dataFim=${encodeURIComponent(dataFim)}`;
        console.log("Chamando API:", url);

        
        const response = await fetch(url);

        if (!response.ok) {
            let errorMsg = `Erro ${response.status}: ${response.statusText}`;
            try {
                
                const errorBody = await response.text();
                errorMsg = `Erro ${response.status}: ${errorBody || response.statusText}`;
            } catch (e) {  }
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
    tableBody.innerHTML = ''; 

    if (!presencas || presencas.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="4" style="text-align: center;">Nenhum registo de presença encontrado para o período selecionado.</td></tr>';
        return;
    }

    

    presencas.forEach(presenca => {
        
        
        const coristaNome = presenca.corista ? presenca.corista.nome : 'Corista Desconhecido';
        const agendaData = presenca.agenda ? presenca.agenda.data : 'N/D';
        const agendaLocal = presenca.agenda ? presenca.agenda.local : 'N/D';
        const presenteStatus = presenca.presente ? 'Sim' : 'Não';

        
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

async function loadDashboardData() {
    const proximoEnsaioEl = document.getElementById('proximo-ensaio');
    const proximaApresentacaoEl = document.getElementById('proxima-apresentacao');
    const totalCoristasEl = document.getElementById('total-coristas');

    
    if (!proximoEnsaioEl || !proximaApresentacaoEl || !totalCoristasEl) {
        
        return; 
    }

    try {
        
        const response = await fetch(`${API_BASE_URL}/dashboard`);
        if (!response.ok) {
           throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json(); 

        
        proximoEnsaioEl.textContent = data.proximoEnsaio || 'N/D';
        proximaApresentacaoEl.textContent = data.proximaApresentacao || 'N/D';
        totalCoristasEl.textContent = data.totalCoristas !== undefined ? data.totalCoristas : 'Erro';

    } catch (error) {
        console.error("Erro ao buscar dados do dashboard:", error);
        
        proximoEnsaioEl.textContent = 'Erro ao carregar';
        proximaApresentacaoEl.textContent = 'Erro ao carregar';
        totalCoristasEl.textContent = 'Erro';
    }
}

async function loadDashboardData() {
    const proximoEnsaioEl = document.getElementById('proximo-ensaio');
    const proximaApresentacaoEl = document.getElementById('proxima-apresentacao');
    const totalCoristasEl = document.getElementById('total-coristas');
    const totalMusicosEl = document.getElementById('total-musicos');

    if (!proximoEnsaioEl || !proximaApresentacaoEl || !totalCoristasEl || !totalMusicosEl) {
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/dashboard`);
        if (!response.ok) {
           throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json(); 

        proximoEnsaioEl.textContent = data.proximoEnsaio || 'N/D';
        proximaApresentacaoEl.textContent = data.proximaApresentacao || 'N/D';
        totalCoristasEl.textContent = data.totalCoristas !== undefined ? data.totalCoristas : 'Erro';
        totalMusicosEl.textContent = data.totalMusicos !== undefined ? data.totalMusicos : 'Erro';

    } catch (error) {
        console.error("Erro ao buscar dados do dashboard:", error);
        proximoEnsaioEl.textContent = 'Erro ao carregar';
        proximaApresentacaoEl.textContent = 'Erro ao carregar';
        totalCoristasEl.textContent = 'Erro';
        totalMusicosEl.textContent = 'Erro';
    }
}
async function loadMusicosForPresenca(agendaId) {
    const musicosListDiv = document.getElementById('musicos-presenca-list');
    if (!musicosListDiv) return;

    musicosListDiv.innerHTML = '<p>Carregando músicos...</p>';

    try {
        const response = await fetch(`${API_BASE_URL}/musicos`);
        if (!response.ok) throw new Error('Falha ao buscar músicos');
        const musicos = await response.json();

        if (!musicos || musicos.length === 0) {
            musicosListDiv.innerHTML = '<p>Nenhum músico encontrado.</p>';
            return;
        }

        musicosListDiv.innerHTML = '';

        musicos.forEach(musico => {
            if (musico.ativo) {
                const div = document.createElement('div');
                div.style.marginBottom = '8px';
                const checkbox = document.createElement('input');
                checkbox.type = 'checkbox';
                checkbox.id = `musico-${musico.id}`;
                checkbox.value = musico.id;
                checkbox.dataset.musicoId = musico.id;
                checkbox.checked = false;

                const label = document.createElement('label');
                label.htmlFor = `musico-${musico.id}`;
                label.textContent = ` ${musico.nome} (${musico.instrumento || 'N/D'})`;
                label.style.marginLeft = '5px';

                div.appendChild(checkbox);
                div.appendChild(label);
                musicosListDiv.appendChild(div);
            }
        });

    } catch (error) {
        console.error("Erro ao carregar lista de músicos para presença:", error);
        musicosListDiv.innerHTML = '<p>Erro ao carregar músicos.</p>';
    }
}