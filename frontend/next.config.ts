const nextConfig = {
  output: "standalone",
  reactStrictMode: true,
  experimental: {
    appDir: true,
  },
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
}

export default nextConfig
