package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.ShadowGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.ShadowsPower;

public class ShadowWave
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:ShadowWave";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/ShadowAtt.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 1;
    private static final int SHADOWS_AMT = 1;
    private static final int UPGRADE_SHADOWS_AMT = 1;

    public ShadowWave() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.COMMON, CardTarget.SELF);
		this.isMultiDamage = true;
        this.baseDamage = this.damage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = SHADOWS_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage,
				this.damageTypeForTurn,
				AbstractGameAction.AttackEffect.FIRE));
        addToBot(new ApplyPowerAction(p, p, new ShadowsPower(p, this.magicNumber)));
        addToBot(new ShadowGene().getChannelAction());
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShadowWave();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_SHADOWS_AMT);
        }
    }
}