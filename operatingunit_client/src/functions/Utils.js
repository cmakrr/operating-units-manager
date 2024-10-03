const SPACE_REGEXP = /\s+/g;

export function compareStrings(a, b) {
    return a?.replace(SPACE_REGEXP, '').localeCompare(b?.replace(SPACE_REGEXP, ''), undefined, {sensitivity: 'base'});
}