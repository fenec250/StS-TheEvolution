package evolutionmod.cardsV1;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.common.RefundAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.cards.GlowingCard;
import evolutionmod.orbsV1.BeastGene;
import evolutionmod.orbsV1.MerfolkGene;
import evolutionmod.patches.EvolutionEnum;

public class SeaWolf
        extends BaseEvoCard implements GlowingCard {
    public static final String cardID = "SeaWolf";
    public static final String ID = "evolutionmod:"+cardID;
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("evolutionmod:"+cardID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/SeaWolf.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 4;
    private static final int UPGRADE_DAMAGE_AMT = 2;
    private static final int MERFOLK_BLOCK_AMT = 3;
    private static final int UPGRADE_MERFOLK_BLOCK_AMT = 1;

    public SeaWolf() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, EvolutionEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.block = this.baseBlock = MERFOLK_BLOCK_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (isPlayerInThisForm(MerfolkGene.ID)) {
            addToBot(new GainBlockAction(p, this.block));
        }
        addToBot(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        formEffect(BeastGene.ID, () -> addToBot(new RefundAction(this, 1)));
        formEffect(MerfolkGene.ID);
    }

    @Override
    public AbstractCard makeCopy() {
        return new SeaWolf();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
            this.upgradeBlock(UPGRADE_MERFOLK_BLOCK_AMT);
        }
    }

	@Override
	public int getNumberOfGlows() {
		return 2;
	}

	@Override
	public boolean isGlowing(int glowIndex) {
		return true;
	}

	@Override
	public Color getGlowColor(int glowIndex) {
		switch (glowIndex) {
			case 0:
				return isPlayerInThisForm(BeastGene.ID) ? BeastGene.COLOR.cpy()
						: BLUE_BORDER_GLOW_COLOR.cpy();
			case 1:
				return isPlayerInThisForm(MerfolkGene.ID, BeastGene.ID) ? MerfolkGene.COLOR.cpy()
						: BLUE_BORDER_GLOW_COLOR.cpy();
			default:
				return BLUE_BORDER_GLOW_COLOR.cpy();
		}
	}
}