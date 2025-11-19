import { zodResolver } from '@hookform/resolvers/zod'
import { useForm } from 'react-hook-form'
import z from 'zod'

const formSchema = z.object({
    tableNumber: z.string().min(1, { message: "Nome é um campo obrigatório." }),
    itemsIds: z.array(z.number()).min(1, {
        message: "Selecione pelo menos um item.",
    }),
    status: z.string()
})

export interface UseDialogOrderFormProps {
    initialValues?: {
        tableNumber?: string;
        itemsIds?: number[];
        status: string;
    }
}

export type DialogOrderFormData = z.infer<typeof formSchema>;

export function useDialogOrderForm({ initialValues }: UseDialogOrderFormProps){
    return useForm<DialogOrderFormData>({
        resolver: zodResolver(formSchema),
        defaultValues: initialValues || {
            tableNumber: "",
            itemsIds: [],
            status: "ORDERED"
        }
    })
}