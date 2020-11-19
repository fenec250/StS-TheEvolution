package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.patches.AbstractCardEnum;

public class Eruption
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Eruption";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/LavafolkAtt.png";
    private static final int COST = 2;
    private static final int DAMAGE_AMT = 18;
    private static final int UPGRADE_DAMAGE_AMT = 5;
    private static final int ERUPTION_AMT = 2;
    private static final int UPGRADE_ERUPTION_AMT = 1;

    public Eruption() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.RARE, CardTarget.ALL_ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.isMultiDamage = true;
        this.magicNumber = this.baseMagicNumber = ERUPTION_AMT;
    }

    @Override
    public boolean canPlay(AbstractCard card) {
        if (card.cardID.equals(ID)
            && !BaseEvoCard.isPlayerInThisForm(LavafolkGene.ID)) {
            return false;
        }
        return super.canPlay(card);
    }

    @Override
    protected String getCantPlayMessage() {
        if (!BaseEvoCard.isPlayerInThisForm(LavafolkGene.ID)) {
            return "A Lavafolk gene is required to play this card.";
        }
        return super.getCantPlayMessage();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (BaseEvoCard.isPlayerInThisForm(LavafolkGene.ID)) {
            addToBot(new DamageAllEnemiesAction(p, this.multiDamage,
                    this.damageTypeForTurn,
                    AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            for (int i = 0; i < this.magicNumber; ++i) {
                addToBot(new ChannelAction(new LavafolkGene()));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Eruption();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
            this.upgradeMagicNumber(UPGRADE_ERUPTION_AMT);
        }
    }
}