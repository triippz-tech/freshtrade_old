const flags = [
  {
    code: 'EN',
    flag: 'πΊπΈ',
  },
  {
    code: 'FR',
    flag: 'π«π·',
  },
];

export const findFlag = (code: string): string => {
  const found = flags.find(flagCode => flagCode.code === code.toUpperCase());
  if (found !== undefined) return found.flag;
  return '';
};
