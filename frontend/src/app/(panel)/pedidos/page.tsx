import { Suspense } from "react"
import PedidosContent from "./_components/pedidos-content"

export default function OrdersPage() {
  return (
    <Suspense fallback={<p>Carregando...</p>}>
        <div className="mt-5">
        <PedidosContent />
        </div>
    </Suspense>
  )
}


