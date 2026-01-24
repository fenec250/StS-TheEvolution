package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.MerfolkGene2;
import evolutionmod.patches.EvolutionEnum;

public class SeaWolf2
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:SeaWolf";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/SeaWolf.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 3;
    private static final int BEAST_BLOCK_AMT = 3;
    private static final int DAMAGE_PER_3_AMT = 1;
    private static final int UPGRADE_DAMAGE_PER_3_AMT = 1;

    public SeaWolf2() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = DAMAGE_PER_3_AMT;
        this.block = this.baseBlock = BEAST_BLOCK_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (isPlayerInThisForm(MerfolkGene2.ID)) {
            addToBot(new GainBlockAction(p, this.block));
        }
        addToBot(new DamageAction(
						m, new DamageInfo(p, damage, damageTypeForTurn),
						AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        formEffect(MerfolkGene2.ID);
    }

	@Override
	public void applyPowers() {
		alterDamageAround(super::applyPowers);
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		alterDamageAround(() -> super.calculateCardDamage(mo));
	}

	private void alterDamageAround(Runnable supercall) {
		this.baseDamage = DAMAGE_AMT + ((AbstractDungeon.player.currentBlock + (isPlayerInThisForm(MerfolkGene2.ID) ? this.block : 0)) / 3) * this.magicNumber;
		supercall.run();
		this.baseDamage = DAMAGE_AMT;
		this.isDamageModified = this.damage != this.baseDamage;
	}

    @Override
    public AbstractCard makeCopy() {
        return new SeaWolf2();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_DAMAGE_PER_3_AMT);
			initializeDescription();
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