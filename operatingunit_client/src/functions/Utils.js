const SPACE_REGEXP = /\s+/g;

export function compareNullableStrings(a, b) {
    if(a === b){
        return 0;
    }
    if(a === null){
        return -1;
    }
    if(b === null){
        return 1;
    }
    return compareStrings(a, b);
}

export function compareStrings(a, b) {
    return a?.replace(SPACE_REGEXP, '').localeCompare(b?.replace(SPACE_REGEXP, ''), undefined, {sensitivity: 'base'});
}