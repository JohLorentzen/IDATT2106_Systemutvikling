/**
 * A general number formatter which transforms large numbers to a more readable version
 * and limits the number of decimals to only two.
 * @param value the number which is displayed.
 */
export function formatNumber(value: number) {
  return new Intl.NumberFormat('de-AT', { maximumFractionDigits: 2, useGrouping: true }).format(
    value
  )
}
