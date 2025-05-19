function compareNullable(a, b){
    if(a === b){
        return 0;
    }
    if(a === null){
        return -1;
    }
    if(b === null){
        return 1;
    }
    return undefined;
}

export const compareNumbers = (a,b) => {
    let result = compareNullable(a, b);
    if(result !== undefined){
        return result;
    }
    return a > b ? 1 : -1;
}

export const compareDates = (a, b) => {
    let result = compareNullable(a, b);
    if(result !== undefined){
        return result;
    }
    return a.localeCompare(b);
}

