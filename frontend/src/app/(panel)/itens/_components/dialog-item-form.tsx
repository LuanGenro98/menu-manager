import { zodResolver } from '@hookform/resolvers/zod'
import { useForm } from 'react-hook-form'
import z from 'zod'

const formSchema = z.object({
    name: z.string().min(1, { message: "Nome é um campo obrigatório." }),
    description: z.string().min(1, { message: "Descrição é um campo obrigatório" }),
    price: z.string().min(1, { message: "Preço é um campo obrigatório" }),
    category_id: z.string(),
    image: z.instanceof(File).optional().nullable(),
})

export interface UseDialogItemFormProps {
    initialValues?: {
        name: string;
        description: string;
        price: string;
        category_id: string;
        image?: File | null; 
    }
}

export type DialogItemFormData = z.infer<typeof formSchema>;

export function useDialogItemForm({ initialValues }: UseDialogItemFormProps){
    return useForm<DialogItemFormData>({
        resolver: zodResolver(formSchema),
        defaultValues: initialValues || {
            name: "",
            description: "",
            price: "",
        }
    })
}