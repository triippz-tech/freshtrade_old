export const truncateString = (str: string, maxLength: number, useWordBoundary: boolean): string => {
  if (str !== null) {
    if (str.length <= maxLength) {
      return str;
    }
    const subString = str.substr(0, maxLength - 1); // the original check
    return (useWordBoundary ? subString.substr(0, subString.lastIndexOf(' ')) : subString) + '...';
  } else {
    return str;
  }
};

export const toTitleCase = (inString: string): string => {
  if (inString === null) return null;
  return inString.replace('_', ' ').replace(/(\w)(\w*)/g, (_, firstCharacter, otherChars) => firstCharacter + otherChars.toLowerCase());
};

export const toEnumCase = (inString: string): string => {
  if (inString === null) return null;
  return inString.replace(' ', '_').toUpperCase();
};
