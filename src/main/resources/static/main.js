function getParishFormHtml(
    formId, 
    nameInputId, 
    parishCodeInputId, 
    infectionRateInputId, 
    closingDateInputId, 
    saveButtonId, 
    cancelButtonId,
    name = "",
    code = "",
    infectionRate = "",
    closing = ""
) {
    return `
            <form id="${formId}">
                <div class="row">
                    <div class="col-6">
                        <label for="${nameInputId}" class="form-label">
                            Sogn Navn
                        </label>
                        <input id="${nameInputId}" type="text" class="form-control" value="${name}" required>
                    </div>
                    <div class="col-2">
                        <label for="${parishCodeInputId}" class="form-label">
                            Sogn Kode
                        </label>
                        <input id="${parishCodeInputId}" type="number" class="form-control" min="1" value="${code}" required>
                    </div>
                    <div class="col-2">
                        <label for="${infectionRateInputId}" class="form-label">
                            Smitte Niveau
                        </label>
                        <input id="${infectionRateInputId}" type="number" step="1" max="5" min="1"  class="form-control" value="${infectionRate}" required>
                    </div>
                    <div class="col-2">
                        <label for="${closingDateInputId}" class="form-label">
                            Lukke dato
                        </label>
                        <input id="${closingDateInputId}" type="date" class="form-control" value="${closing}">
                    </div>
                </div>
                <div class="d-flex flex-row-reverse">
                    <button type="submit" class="btn btn-primary mt-3 mx-3" id="${saveButtonId}">
                        Gem
                    </button>
                    <button type="button" class="btn btn-secondary mt-3 mx-3" id="${cancelButtonId}">
                        Cancel
                    </button>
                </div>
            </form>
    `;
}


class ParishView {
    constructor(parish, bindingId, parentView){
        this.setParishValues(parish)
        this.parentView = parentView;

        this.bindingId = bindingId;

        this.id = bindingId + "-" + this.code;
        this.collapseId = "collapse-form-" + this.id;
        this.updateFormContainerId = "update-form-container-" + this.id;
        this.deleteButtonId = "delete-button-" + this.id;
        
        this.draw();
    }

    setParishValues(parish){
        this.name = parish.name;
        this.code = parish.parishCode;
        this.infectionRate = parish.infectionRate;
        this.closing = parish.closing;
        this.parish = parish;
    }

    getInfectionTextClass(){
        if(this.infectionRate > 2){
            return "text-danger";
        }
        else if(this.infectionRate > 1){
            return "text-warning"
        }
        return "text-success";
    }


    isClosed() {
        if(this.closing === null){
            return false;
        }
        const closingDate = new Date(this.closing);
        const now = new Date();
        return closingDate <= now;
    }

    unDraw(){
        $(`#${this.id}`).remove();
    }

    drawUpdateForm(){
        const nameInputId = "perish-name-" + this.id;
        const parishCodeInputId = "perish-code-" + this.id;
        const infectionRateInputId = "infection-rate-" + this.id;
        const closingDateInputId = "closing-date-" + this.id;
        const saveButtonId = "save-button-" + this.id;
        const cancelButtonId = "cancel-button-" + this.id;
        const formId = "form-" + this.id;

        let html = `
            <div id="${this.collapseId}" class="card-body collapse">
                ${getParishFormHtml(
                    formId, 
                    nameInputId, 
                    parishCodeInputId, 
                    infectionRateInputId, 
                    closingDateInputId, 
                    saveButtonId, 
                    cancelButtonId,
                    this.name,
                    this.code,
                    this.infectionRate,
                    this.closing
                )}
                
            </div>
        `;

        $(`#${this.updateFormContainerId}`).html(html);

        $(`#${nameInputId}`).on("input", (e) => {
            const value = e.target.value;
            this.parish.name = value;
        });

        $(`#${parishCodeInputId}`).on("input", (e) => {
            const value = e.target.value;
            this.parish.parishCode = parseInt(value);
        });

        $(`#${infectionRateInputId}`).on("input", (e) => {
            const value = e.target.value;
            this.parish.infectionRate = parseInt(value);
        });

        $(`#${closingDateInputId}`).on("input", (e) => {
            const value = e.target.value;
            this.parish.closing = value;
        });

        $(`#${cancelButtonId}`).on("click", () => {
            $(`#${this.collapseId}`).collapse("hide");
        })

        $(`#${formId}`).submit((e) => {
            e.preventDefault();

            $.ajax({
                url: "/sogn",
                method: "PUT",
                data: JSON.stringify(this.parish),
                dataType: "json",
                contentType: "application/json",
                success: (response) => {
                    $(`#${this.collapseId}`).collapse("hide");
                    response.commune = this.parish.commune;
                    this.setParishValues(response);
                    this.reDraw()
                    this.parentView.updateParish(response);
                },
                error: (error) => {
                    alert(error.responseText);
                }
            })
        })
    }

    getHtml() {
        return `
            <div class="card-body row">
                <div class="col-10">
                    <h5 class="card-title">
                        ${this.name}
                    </h5>
                    <h6 class="card-subtitle text-muted">
                        Sognekode: ${this.code}
                    </h6>
                    <p class="card-text">
                        Smitteniveau: <b class="${this.getInfectionTextClass()}">${this.infectionRate}</b>
                        <br/>
                        Nedlukingsdato: ${this.closing}
                        <br/>
                        Sogn lukket: <input type="checkbox" disabled ${this.isClosed() ? "checked" : ""}>
                    </p>
                </div>
                <div class="col-2 text-right">
                    <button class="btn btn-primary" data-bs-toggle="collapse" data-bs-target="#${this.collapseId}" aria-expanded="false" aria-controls="${this.collapseId}">
                        Redigér
                    </button>
                    <button class="btn btn-danger" id="${this.deleteButtonId}">
                        Fjern
                    </button>
                </div>
            </div>
            <div id="${this.updateFormContainerId}">
                
            </div>
        `;
    }

    draw(){
        let html = `
            <div id="${this.id}" class="card my-2">
                ${this.getHtml()}
            </div>
        `
        $(`#${this.bindingId}`).append(html);
        this.drawUpdateForm();

        $(`#${this.deleteButtonId}`).on("click", () => {
            $.ajax({
                url: `/sogn/${this.code}`,
                method: "DELETE",
                success: (response) => {
                    this.unDraw();
                    this.parentView.deleteParish(this.parish);
                },
                error: (err) => {
                    alert("Could not delete...")
                }
            })
        })
    }

    reDraw(){
        console.log("reDrawing");
        $(`#${this.id}`).html(this.getHtml());
        this.drawUpdateForm();


        $(`#${this.deleteButtonId}`).on("click", () => {
            $.ajax({
                url: `/sogn/${this.code}`,
                method: "DELETE",
                success: (response) => {
                    this.unDraw();
                },
                error: (err) => {
                    alert("Could not delete...")
                }
            })
        })
    }
}

class CommuneView {
    constructor(commune, bindingId){
        this.commune = commune;
        this.bindingId = bindingId;
        this.newParish = {commune: this.commune.communeCode};

        this.parishViews = [];
        this.id = "commune-container-" + commune.communeCode;
        this.parisFormContainer = "parish-form-container-" + this.id;
        this.collapseId = "collapse-" + this.id
        this.infectionRateId = "commune-infection-rate-" + this.id;

        this.draw();

        for(let parish of commune.parishes){
            parish.commune = commune.communeCode;
            this.parishViews.push(new ParishView(parish, this.id, this));
        }
    }

    getInfectionRateHtml(){
        const infectionRate = this.getInfectionRateAverage();
        const infectionRateColor = this.getInfectionRateColor(infectionRate);
        return `
            Smitte niveau:  <b class="${infectionRateColor}">${this.getInfectionRateAverage()}</b>
        `
    }

    addNewParish(parish){
        this.commune.parishes.push(parish);
        this.parishViews.push(new ParishView(parish, this.id, this));
        $(`#${this.infectionRateId}`).html(this.getInfectionRateHtml())
    }

    updateParish(newParish){
        let oldParish = this.commune.parishes.find(p => p.parishCode === newParish.parishCode);
        let i = this.commune.parishes.indexOf(oldParish);
        this.commune.parishes[i] = newParish;
        $(`#${this.infectionRateId}`).html(this.getInfectionRateHtml())
    }

    deleteParish(deletedParish){
        this.commune.parishes = this.commune.parishes.filter(p => p.parishCode !== deletedParish.parishCode);
        $(`#${this.infectionRateId}`).html(this.getInfectionRateHtml())
    }

    drawParishForm(){
        const nameInputId = "perish-name-" + this.id;
        const parishCodeInputId = "perish-code-" + this.id;
        const infectionRateInputId = "infection-rate-" + this.id;
        const closingDateInputId = "closing-date-" + this.id;
        const saveButtonId = "save-button-" + this.id;
        const cancelButtonId = "cancel-button-" + this.id;
        const formId = "form-" + this.id;
        let html = `
                <button class="btn btn-primary" data-bs-toggle="collapse" data-bs-target="#${this.collapseId}" aria-expanded="false" aria-controls="${this.collapseId}">
                    Tilføj Sogn
                </button>
                <div class="collapse mt-3" id="${this.collapseId}">
                    ${getParishFormHtml(formId, nameInputId, parishCodeInputId, infectionRateInputId, closingDateInputId, saveButtonId, cancelButtonId)}
                <div/>
        `
        $(`#${this.parisFormContainer}`).html(html);

        $(`#${nameInputId}`).on("input", (e) => {
            const value = e.target.value;
            this.newParish.name = value;
        });

        $(`#${parishCodeInputId}`).on("input", (e) => {
            const value = e.target.value;
            this.newParish.parishCode = parseInt(value);
        });

        $(`#${infectionRateInputId}`).on("input", (e) => {
            const value = e.target.value;
            this.newParish.infectionRate = parseInt(value);
        });

        $(`#${closingDateInputId}`).on("input", (e) => {
            const value = e.target.value;
            this.newParish.closing = value;
        });

        $(`#${cancelButtonId}`).on("click", () => {
            $(`#${this.collapseId}`).collapse("hide");
        })

        $(`#${formId}`).submit((e) => {
            e.preventDefault();

            $.ajax({
                url: "/sogn",
                method: "POST",
                data: JSON.stringify(this.newParish),
                dataType: "json",
                contentType: "application/json",
                success: (response) => {
                    response.commune = this.commune.communeCode;
                    this.addNewParish(response)
                    $(`#${this.collapseId}`).collapse("hide");
                },
                error: (error) => {
                    alert(error.responseText);
                }
            })
        })
    }

    getInfectionRateAverage(){
        if(this.commune.parishes.length === 0){
            return 0;
        }
        let sum = 0;
        this.commune.parishes.forEach(p => {
            sum += p.infectionRate;
        })
        console.log(this.commune.parishes);
        return ( sum / this.commune.parishes.length ).toFixed(0);
    }

    getInfectionRateColor(infectionRate){
        if(infectionRate > 2){
            return "text-danger";
        }
        else if(infectionRate > 1){
            return "text-warning"
        }
        return "text-success";
    }


    draw(){
        let html = `
            <div class="card my-3">
                <div class="card-body">
                    <h2 class="card-title">
                        ${this.commune.name}
                    </h2>
                    <h4 class="card-subtitle text-muted">
                        Kommunekode: ${this.commune.communeCode}
                    </h4>
                    <p id="${this.infectionRateId}">
                        ${this.getInfectionRateHtml()}
                    </p>
                </div>
                <div id="${this.id}" class="card-body">
                    
                </div>
                <div id=${this.parisFormContainer} class="card-body">
                    
                </div>
            </div>
        `;
        $(`#${this.bindingId}`).append(html);
        this.drawParishForm();
    }
}


async function getAllCommunes(){
    const result = await $.ajax({
        url: "/kommune",
        method: "GET",
        dataType: "json"
    });
    return result;
}

async function main(){
    const communes = await getAllCommunes();
    const communeContainerId = "commune-container";
    let communeViews = [];
    for(commune of communes){
        communeViews.push(new CommuneView(commune, communeContainerId));
    }
}

main();
