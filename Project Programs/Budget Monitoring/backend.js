const ExpenseTable=document.getElementById('ExpenseTable');
const CalculateExpenseButton=document.getElementById('CalculateExpenseButton');
CalculateExpenseButton.addEventListener('click',calculateExpense);
function calculateExpense(){
    const ExpenseCause=document.getElementById('ExpenseCause').value;
    const ExpenseDate=document.getElementById('ExpenseDate').value;
    const ExpenseTime=document.getElementById('ExpenseTime').value;
    const ExpenseAmount=parseFloat(document.getElementById('ExpenseAmount').value);
    const newRow=ExpenseTable.insertRow();
    newRow.insertCell().textContent=ExpenseCause;
    newRow.insertCell().textContent=ExpenseDate;
    newRow.insertCell().textContent=ExpenseTime;
    newRow.insertCell().textContent=ExpenseAmount;
    let total_amt_spend=0;
    for(let i=1;i<ExpenseTable.rows.length;i++){
        total_amt_spend+=parseFloat(ExpenseTable.rows[i].cells[3].textContent);
    }
    let maxcost_expense=' ';
    let maxcost=0;
    for(let i=1;i<ExpenseTable.rows.length;i++){
        const cost=parseFloat(ExpenseTable.rows[i].cells[3].textContent);
        if(cost > maxcost){
            maxcost=cost;
            maxcost_expense=ExpenseTable.rows[i].cells[0].textContent;
        }
    }
    let mincost_expense=' ';
    let mincost=Infinity;
    for(let i=1;i<ExpenseTable.rows.length;i++){
        const cost=parseFloat(ExpenseTable.rows[i].cells[3].textContent);
        if(cost < mincost){
            mincost=cost;
            mincost_expense=ExpenseTable.rows[i].cells[0].textContent;
        }
    }
    document.getElementById('ExpenseCause').value='';
    document.getElementById('ExpenseDate').value='';
    document.getElementById('ExpenseTime').value='';
    document.getElementById('ExpenseAmount').value='';
    const sortedExpense=[];
    for(let i=1;i<ExpenseTable.rows.length;i++){
        sortedExpense.push({
            ExpenseCause: ExpenseTable.rows[i].cells[0].textContent,
            cost: parseFloat(ExpenseTable.rows[i].cells[3].textContent)
        });
        sortedExpenses.sort((a,b) => a.cost - b.cost);
        const sortedExpenseList=document.getElementById('sortedExpense');
        sortedExpenseList.innerHTML='';
        for(const expense of sortedExpense){
            const item=document.createElement('li');
            item.textContent=`${expense.ExpenseCause}: ${expense.cost}`;
            sortedExpenseList.appendChild(item);
        }
    }
}