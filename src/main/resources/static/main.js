class ParishView {
    constructor(parish, bindingId){
        this.parish = parish;

        this.name = parish.name;
        this.code = parish.parishCode;
        this.infectionRate = parish.infectionRate;
        this.closing = parish.closing;
        this.bindingId = bindingId;

        this.id = bindingId + "-" + this.code;
        
        this.draw();
    }

    getInfectionTextClass(){
        if(this.infectionRate > 3){
            return "text-danger";
        }
        else if(this.infectionRate > 2){
            return "text-warning"
        }
        return "text-success";
    }

    getClosingDate(){
        if(this.closing === null){
            return " - ";
        }
        const year = this.closing.year;
        const month = this.closing.monthValue <= 9 ? "0"+this.closing.monthValue : this.closing.monthValue;
        const day = this.closing.dayOfMonth <= 9 ? "0"+this.closing.dayOfMonth : this.closing.dayOfMonth;
        return `${year}-${month}-${day}`;
    }

    isClosed() {
        if(this.closing === null){
            return false;
        }
        const closingDate = new Date(this.getClosingDate());
        const now = new Date();
        return closingDate <= now;
    }

    unDraw(){
        $(`#${this.id}`).remove();
    }

    draw(){
        const deleteButtonId = "delete-button-" + this.id;
        let html = `
            <div id="${this.id}" class="card my-2">
                <div class="card-body row">
                    <div class="col-10">
                        <h5 class="card-title">
                            ${this.name}
                        </h5>
                        <h6 class="card-subtitle text-muted">
                            Sogn Kode: ${this.code}
                        </h6>
                        <p class="card-text">
                            Smitteniveau: <b class="${this.getInfectionTextClass()}">${this.infectionRate}</b>
                            <br/>
                            Nedlukingsdato: ${this.getClosingDate()}
                            <br/>
                            Sogn lukket: <input type="checkbox" disabled ${this.isClosed() ? "checked" : ""}>
                        </p>
                    </div>
                    <div class="col-2 text-right">
                        <button class="btn btn-primary">
                            Redigér
                        </button>
                        <button class="btn btn-danger" id="${deleteButtonId}">
                            Fjern
                        </button>
                    </div>
                </div>
            </div>
        `
        $(`#${this.bindingId}`).append(html);

        $(`#${deleteButtonId}`).on("click", () => {
            console.log("HEREJjA;");
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

        this.draw();

        for(let parish of commune.parishes){
            this.parishViews.push(new ParishView(parish, this.id));
        }
    }

    addNewParish(parish){
        this.commune.parishes.push(parish);
        this.parishViews.push(new ParishView(parish, this.id));
    }

    drawParishForm(){
        const nameInputId = "perish-name-" + this.id;
        const parishCodeInputField = "perish-code-" + this.id;
        const infectionRateInputId = "infection-rate-" + this.id;
        const closingDateInputId = "closing-date-" + this.id;
        const saveButtonId = "save-button-" + this.id;
        const cancelButtonId = "cancel-button-" + this.id;
        const formId = "form-" + this.id;
        let html = `
                <button class="btn btn-primary" data-bs-toggle="collapse" data-bs-target="#${this.collapseId}" aria-expanded="false" aria-controls="${this.collapseId}">
                    Tilføj Sogn
                </button>

                <form id="${formId}">
                    <div class="collapse mt-3" id="${this.collapseId}">
                        <div class="row">
                            <div class="col-6">
                                <label for="${nameInputId}" class="form-label">
                                    Sogn Navn
                                </label>
                                <input id="${nameInputId}" type="text" class="form-control">
                            </div>
                            <div class="col-2">
                                <label for="${parishCodeInputField}" class="form-label">
                                    Sogn Kode
                                </label>
                                <input id="${parishCodeInputField}" type="number" class="form-control" min="1">
                            </div>
                            <div class="col-2">
                                <label for="${infectionRateInputId}" class="form-label">
                                    Smitte Niveau
                                </label>
                                <input id="${infectionRateInputId}" type="number" step="0.01" max="100" min="0"  class="form-control">
                            </div>
                            <div class="col-2">
                                <label for="${closingDateInputId}" class="form-label">
                                    Lukke dato
                                </label>
                                <input id="${closingDateInputId}" type="date" class="form-control">
                            </div>
                        </div>
                        <div class="d-flex flex-row-reverse">
                            <button type="submit" class="btn btn-primary mt-3 mx-3" id="${saveButtonId}">
                                Gem
                            </button>
                            <button class="btn btn-secondary mt-3 mx-3" id="${cancelButtonId}">
                                Cancel
                            </button>
                        </div>
                    </div>
                </form>
        `
        $(`#${this.parisFormContainer}`).html(html);

        $(`#${nameInputId}`).on("input", (e) => {
            const value = e.target.value;
            this.newParish.name = value;
        });

        $(`#${parishCodeInputField}`).on("input", (e) => {
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

        $(`#${formId}`).submit((e) => {
            e.preventDefault();

            $.ajax({
                url: "/sogn",
                method: "POST",
                data: JSON.stringify(this.newParish),
                dataType: "json",
                contentType: "application/json",
                success: (response) => {
                    this.addNewParish(response)
                    $(`#${this.collapseId}`).collapse("hide");
                },
                error: (error) => {
                    console.log(error);
                }
            })
        })
    }


    draw(){
        let html = `
            <div class="card my-3">
                <div class="card-body">
                    <h2 class="card-title">
                        ${this.commune.name}
                    </h2>
                    <h4 class="card-subtitle text-muted">
                        Kommune Kode: ${this.commune.communeCode}
                    </h4>
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
