document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("initBtn").addEventListener("click", initRun);
    document.getElementById("resetBtn").addEventListener("click", reset);
    setInterval(loadStatus, 500);
});

async function initRun() {
    const payload = {
        chocolatiersN: parseInt(document.getElementById("nbChocoN").value),
        chocolatiersB: parseInt(document.getElementById("nbChocoB").value),
        tempereusesN: parseInt(document.getElementById("nbTempN").value),
        tempereusesB: parseInt(document.getElementById("nbTempB").value),
        mouleusesN: parseInt(document.getElementById("nbMouleN").value),
        mouleusesB: parseInt(document.getElementById("nbMouleB").value),
    };

    await axios.post("/api/init_run", payload, {
        headers: { "Content-Type": "application/json" }
    });

    loadStatus();
}

async function reset() {
    await axios.post("/api/reset");
    loadStatus();
}

async function loadStatus() {
    const res = await axios.get("/api/status");
    renderChocolatiers(res.data.chocolatiers);
    console.log("tempereuses backend:", res.data.tempereuses);
    renderMachines(res.data.tempereuses, "tempereuse");
    renderMachines(res.data.mouleuses, "mouleuse");
}

function renderChocolatiers(chocolatiers) {
    const groupeN = document.getElementById("groupe-n-chocolatiers");
    const groupeB = document.getElementById("groupe-b-chocolatiers");
    groupeN.innerHTML = "";
    groupeB.innerHTML = "";

    chocolatiers.forEach(choco => {
        const wrapper = document.createElement("div");
        wrapper.className = "choco-wrapper";

        const title = document.createElement("div");
        title.className = "choco-title";
        title.textContent = "Chocolatier: " + choco.id;
        wrapper.appendChild(title);

        const bar = document.createElement("div");
        bar.className = "choco-bar";

        const steps = [
            "AUCUNE", "RUPTURE", "REQUIERE_TEMPEREUSE", "TEMPERE_CHOCOLAT", "DONNE_CHOCOLAT",
            "REQUIERE_MOULEUSE", "REMPLIT", "GARNIT", "FERME"
        ];

        steps.forEach(step => {
            const div = document.createElement("div");
            div.classList.add("step");
            div.textContent = step.replace("_", " ").toLowerCase();

            if (choco.etape === step) div.classList.add("current");
            if (["TEMPERE_CHOCOLAT", "DONNE_CHOCOLAT", "REMPLIT", "GARNIT", "FERME"].includes(step)) {
                div.classList.add("machine-controlled");
            }

            bar.appendChild(div);
        });

        wrapper.appendChild(bar);
        (choco.groupe === "n" ? groupeN : groupeB).appendChild(wrapper);
    });
}

function renderMachines(machines, type) {
    const containerN = document.getElementById(`groupe-n-${type}s`);
    const containerB = document.getElementById(`groupe-b-${type}s`);
    containerN.innerHTML = "";
    containerB.innerHTML = "";

    const stepsMap = {
        tempereuse: ["AUCUNE", "TEMPERE_CHOCOLAT", "DONNE_CHOCOLAT"], 
        mouleuse: ["AUCUNE", "REMPLIT", "GARNIT", "FERME"]
    };

    machines.forEach(machine => {
        const wrapper = document.createElement("div");
        wrapper.className = "machine-wrapper";

        const title = document.createElement("div");
        title.className = "machine-title";
        title.textContent = `${type.toUpperCase()} ${machine.id.slice(0, 5)} (Choco: ${machine.chocolatier_id ? machine.chocolatier_id.slice(0, 5) : '---'})`;
        wrapper.appendChild(title);

        const bar = document.createElement("div");
        bar.className = "machine-bar";
        const steps = stepsMap[type];
        steps.forEach(step => {
            const div = document.createElement("div");
            div.classList.add("step");
            div.textContent = step.toLowerCase();

            if (machine.etape === step) {
                div.classList.add("current");
            }

            bar.appendChild(div);
        });

        wrapper.appendChild(bar);
        (machine.groupe === "n" ? containerN : containerB).appendChild(wrapper);
    });
}

