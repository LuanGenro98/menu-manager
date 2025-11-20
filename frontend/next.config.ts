const nextConfig = {
  output: "standalone",
  reactStrictMode: true,
  experimental: {
    appDir: true,
  },
<<<<<<< HEAD
  images: {
    remotePatterns: [
      {
        protocol: 'https',
        hostname: process.env.S3_URL_BUCKET, 
        port: '',
        pathname: '/uploads/**',
      },
    ],
  },
=======
>>>>>>> 7e57ed6b904f64a89959525d9a234fa0ca3424dd
}

export default nextConfig
