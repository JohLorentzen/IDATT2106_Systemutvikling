/** @type {import('tailwindcss').Config} */
export default {
  'content': ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
  'darkMode': 'media',
  'theme': {
    'extend': {
      'colors': {
        'background': '#f8f0f0',
        'primaryBtn': '#EA6F6F',//'#EF4444FF',
        'hooverBtn': '#D45A5A',//'#B91C1C',
        'primary-text': '#454B60',
        'custom-yellow': '#ffd500',
      },
      'backgroundImage': {
        'backgroundImage': "url('@/assets/MainBackground.svg')",
        'waveBackground': "url('@/assets/WaveBackground.svg')"
      }
    }
  },
  'plugins': [],
}

