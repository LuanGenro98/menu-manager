import { zodResolver } from '@hookform/resolvers/zod'
import { useForm } from 'react-hook-form'
import z from 'zod'

const formSchema = z.object({
    name: z.string().min(1, { message: "Nome é um campo obrigatório." }),
    description: z.string().min(1, { message: "Descrição é um campo obrigatório" }),
})

export interface UseDialogCategoryFormProps {
    initialValues?: {
        name: string;
        description: string;
    }
}

export type DialogCategoryFormData = z.infer<typeof formSchema>;

export function useDialogCategoryForm({ initialValues }: UseDialogCategoryFormProps){
    return useForm<DialogCategoryFormData>({
        resolver: zodResolver(formSchema),
        defaultValues: initialValues || {
            name: "",
            description: "",
        }
    })
}