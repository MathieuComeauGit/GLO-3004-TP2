document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("resetBtn").addEventListener("click", reset);
    document.getElementById("initBtnN").addEventListener("click", () => initGroup('n'));
    document.getElementById("initBtnB").addEventListener("click", () => initGroup('b'));
    loadStatus();
});

async function initGroup(group) {
    const payload = {
        chocolatiers: parseInt(document.getElementById(`nbChoco${group.toUpperCase()}`).value),
        tempereuses: parseInt(document.getElementById(`nbTemp${group.toUpperCase()}`).value),
        mouleuses: parseInt(document.getElementById(`nbMoule${group.toUpperCase()}`).value),
        groupe: group
    };
    await axios.post(`/api/init_groupe`, payload);
    loadStatus();
}

async function reset() {
    await axios.post("/api/reset");
    loadStatus();
}

async function loadStatus() {
    const res = await axios.get("/api/status");
    const data = res.data;
    renderChocolatiers(data.chocolatiers);
    renderTempereuses(data.tempereuses);
    renderMouleuses(data.mouleuses);
}

function renderChocolatiers(chocolatiers) {
    const groupeN = document.getElementById("groupe-n-chocolatiers");
    const groupeB = document.getElementById("groupe-b-chocolatiers");
    groupeN.innerHTML = "";
    groupeB.innerHTML = "";

    chocolatiers.forEach(choco => {
        const div = document.createElement("div");
        div.classList.add("status-card");
        div.innerHTML = `<span><b>Étape:</b> ${choco.etape}</span>`;

        if (choco.nextStep && choco.nextStep !== "BLOCKED") {
            const label = document.createElement("label");
            const checkbox = document.createElement("input");
            checkbox.type = "checkbox";
            checkbox.addEventListener("change", () => changerEtapeChocolatier(choco.id));
            label.appendChild(checkbox);
            label.appendChild(document.createTextNode(` Aller à ${choco.nextStep}`));
            div.appendChild(label);
        }

        (choco.groupe === "n" ? groupeN : groupeB).appendChild(div);
    });
}

function renderTempereuses(tempereuses) {
    const groupeN = document.getElementById("groupe-n-tempereuses");
    const groupeB = document.getElementById("groupe-b-tempereuses");
    groupeN.innerHTML = "";
    groupeB.innerHTML = "";

    tempereuses.forEach(temp => {
        const div = document.createElement("div");
        div.classList.add("status-card");
        div.innerHTML = `<span><b>Étape:</b> ${temp.etape}</span>`;

        if (temp.nextStep && temp.chocolatier_id) {
            const label = document.createElement("label");
            const checkbox = document.createElement("input");
            checkbox.type = "checkbox";
            checkbox.addEventListener("change", () => changerEtapeTempereuse(temp.id));
            label.appendChild(checkbox);
            label.appendChild(document.createTextNode(` Aller à ${temp.nextStep}`));
            div.appendChild(label);
        }

        (temp.groupe === "n" ? groupeN : groupeB).appendChild(div);
    });
}

function renderMouleuses(mouleuses) {
    const groupeN = document.getElementById("groupe-n-mouleuses");
    const groupeB = document.getElementById("groupe-b-mouleuses");
    groupeN.innerHTML = "";
    groupeB.innerHTML = "";

    mouleuses.forEach(moule => {
        const div = document.createElement("div");
        div.classList.add("status-card");
        div.innerHTML = `<span><b>Étape:</b> ${moule.etape}</span>`;

        if (moule.nextStep && moule.chocolatier_id) {
            const label = document.createElement("label");
            const checkbox = document.createElement("input");
            checkbox.type = "checkbox";
            checkbox.addEventListener("change", () => changerEtapeMouleuse(moule.id));
            label.appendChild(checkbox);
            label.appendChild(document.createTextNode(` Aller à ${moule.nextStep}`));
            div.appendChild(label);
        }

        (moule.groupe === "n" ? groupeN : groupeB).appendChild(div);
    });
}

async function changerEtapeChocolatier(id) {
    await axios.post("/api/change_etape", { id });
    loadStatus();
}

async function changerEtapeTempereuse(id) {
    await axios.post("/api/change_etape_tempereuse", { id });
    loadStatus();
}

async function changerEtapeMouleuse(id) {
    await axios.post("/api/change_etape_mouleuse", { id });
    loadStatus();
}
