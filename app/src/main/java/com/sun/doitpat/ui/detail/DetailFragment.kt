package com.sun.doitpat.ui.detail

import com.sun.doitpat.BR
import com.sun.doitpat.R
import com.sun.doitpat.base.BaseFragment
import com.sun.doitpat.databinding.FragmentDetailBinding

class DetailFragment : BaseFragment<FragmentDetailBinding, DetailViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_detail
    override val bindingVariable: Int
        get() = BR.todo
    override val viewModel: DetailViewModel
        get() = DetailViewModel()
}
