import { NextRequest, NextResponse } from 'next/server';
import { LoginSchema } from '@/app/api/login/_components/use-login-form';

export async function POST(request: NextRequest) {
    try {
        const body = await request.json();
        
        const validatedData = LoginSchema.parse(body);
        
        const externalApiResponse = await fetch('https://your-auth-api.com/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${process.env.AUTH_API_KEY}`,
            },
            body: JSON.stringify({
                email: validatedData.email,
                password: validatedData.password,
            }),
        });

        if (!externalApiResponse.ok) {
            const errorData = await externalApiResponse.json().catch(() => ({}));
            
            if (externalApiResponse.status === 401) {
                return NextResponse.json(
                    { success: false, message: 'Invalid credentials' },
                    { status: 401 }
                );
            }
            
            return NextResponse.json(
                { success: false, message: errorData.message || 'Authentication failed' },
                { status: externalApiResponse.status }
            );
        }

        const authData = await externalApiResponse.json();
        
        return NextResponse.json(
            { 
                success: true, 
                message: 'Login successful',
                user: authData.user,
            },
            { status: 200 }
        );
        
    } catch (error) {
        console.error('Login API error:', error);
        
        if (error instanceof Error && error.name === 'ZodError') {
            return NextResponse.json(
                { success: false, message: 'Invalid request data' },
                { status: 400 }
            );
        }
        
        if (error instanceof Error && error.message.includes('fetch')) {
            return NextResponse.json(
                { success: false, message: 'Authentication service unavailable' },
                { status: 503 }
            );
        }
        
        return NextResponse.json(
            { success: false, message: 'Internal server error' },
            { status: 500 }
        );
    }
}