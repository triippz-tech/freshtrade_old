const flags = [
  {
    code: 'EN',
    flag: '🇺🇸',
  },
  {
    code: 'FR',
    flag: '🇫🇷',
  },
];

export const findFlag = (code: string): string => {
  const found = flags.find(flagCode => flagCode.code === code.toUpperCase());
  if (found !== undefined) return found.flag;
  return '';
};
