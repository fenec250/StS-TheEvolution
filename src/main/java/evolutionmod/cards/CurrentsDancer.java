package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.MerfolkGene2;
import evolutionmod.patches.EvolutionEnum;

public class CurrentsDancer
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:CurrentsDancer";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/CurrentsDancerSkill.png";
    private static final int COST = 1;
    private static final int BLOCK_AMT = 5;
    private static final int UPGRADE_BLOCK_AMT = 3;
    private static final int FORM_DRAW_AMT = 1;

    public CurrentsDancer() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.block = this.baseBlock = BLOCK_AMT;
        this.magicNumber = this.baseMagicNumber = FORM_DRAW_AMT;
    }

    @Override
	public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, this.block));
        addToBot(new DrawCardAction(this.magicNumber));
		formEffect(MerfolkGene2.ID, () -> addToBot(new DrawCardAction(FORM_DRAW_AMT)));
	}

    @Override
    public AbstractCard makeCopy() {
        return new CurrentsDancer();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK_AMT);
        }
    }

	@Override
	public void triggerOnGlowCheck() {
		if (isPlayerInThisForm(MerfolkGene2.ID)) {
			this.glowColor = MerfolkGene2.COLOR.cpy();
		} else {
			this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
		}
	}
}